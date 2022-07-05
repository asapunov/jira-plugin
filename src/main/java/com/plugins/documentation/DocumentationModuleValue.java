package com.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
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

//    public IssueService.IssueResult validation(Issue issue, int actionId){
//        IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
//        IssueService.TransitionValidationResult transitionValidationResult = issueService.validateTransition(currentUser, issue.getId(), actionId, issueInputParameters);
//        if (transitionValidationResult.isValid()) {
//            IssueService.IssueResult transitionResult = issueService.transition(currentUser, transitionValidationResult);
//            if (transitionResult.isValid())
//                log.debug("Transitioned issue" + issue + "through action $actionId");
//            else
//                log.debug("Transition result is not valid");
//            return transitionResult;
//        }
//        else {
//            log.debug("The transitionValidation is not valid");
//            return null;
//        }
//    }

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
