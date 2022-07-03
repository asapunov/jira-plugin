package com.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.velocity.NumberTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.URI;
import java.util.*;

public class DocumentationModuleValue extends AbstractJiraContextProvider {

    private static final Logger log = LoggerFactory.getLogger(DocumentationModuleValue.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();
    private static final CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
    private static final NumberTool numberTool = new NumberTool(new Locale("ru", "RU"));
    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue issue = (Issue)jiraHelper.getContextParams().get("issue");
        contextMap.put("customFieldManager", customFieldManager);
        contextMap.put("issueManager", issueManager);
        contextMap.put("issue", issue);
        contextMap.put("number", numberTool);
        contextMap.put("baseURL", getjiraURL());
        contextMap.put("supplierOffers", getSupplierOffers(issue));
        contextMap.put("DMV", this);
        return contextMap;
    }

    private ArrayList<MutableIssue> getSupplierOffers(Issue issue){
        String supplierOffers = getCfValue("Предложения поставщиков", issue);
        assert supplierOffers != null;
        String[] offers = supplierOffers.split(",");
        ArrayList<MutableIssue> issuesSO = new ArrayList<>();
        for (String issues: offers){
            issues = issues.trim();
            issuesSO.add(issueManager.getIssueObject(issues));
        }
        return issuesSO;
    }

    public IssueService.IssueResult validation(Issue issue, int actionId){
        IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
        IssueService.TransitionValidationResult transitionValidationResult = issueService.validateTransition(currentUser, issue.getId(), actionId, issueInputParameters);
        if (transitionValidationResult.isValid()) {
            IssueService.IssueResult transitionResult = issueService.transition(currentUser, transitionValidationResult);
            if (transitionResult.isValid())
                log.debug("Transitioned issue" + issue + "through action $actionId");
            else
                log.debug("Transition result is not valid");
            return transitionResult;
        }
        else {
            log.debug("The transitionValidation is not valid");
            return null;
        }

    }

    public void updateIssueStatus(Issue issue, String status) {
        IssueRestClient client = getJiraRestClient().getIssueClient();

        Iterable<Transition> transitions = client.getTransitions((com.atlassian.jira.rest.client.api.domain.Issue) issue).claim();

        for(Transition t : transitions){
            if(t.getName().equals(status)) {
                TransitionInput input = new TransitionInput(t.getId());
                client.transition((com.atlassian.jira.rest.client.api.domain.Issue) issue, input).claim();
                return;
            }
        }
    }


// you can ignore below if you know how to get IssueRestClient and Issue

    public JiraRestClient getJiraRestClient() {
        AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

        JiraRestClient client = factory.createWithBasicHttpAuthentication(URI.create(getjiraURL()), "admin", "admin");

        return client;
    }

    public Issue getIssue(String issueKey) {
        IssueRestClient client = getJiraRestClient().getIssueClient();
        return (Issue) client.getIssue(issueKey).claim();
    }

    private String getCfValue(String cfName, Issue issue) {
        CustomField customField = customFieldManager.getCustomFieldObjectByName(cfName);
        try {
            String cfValue = (customField != null ? customField.getValue(issue).toString() : null);
            log.warn(cfValue);
            return cfValue;
        }
        catch (Exception e) {
            log.warn("Field is empty");
            return null;
        }
    }

    private String getjiraURL() {
        return ComponentAccessor.getApplicationProperties().getString("jira.baseurl");
    }

}
