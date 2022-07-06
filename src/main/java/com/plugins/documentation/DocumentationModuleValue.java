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
    private static final FieldGetter fieldGetter = new FieldGetter();
    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue issue = (Issue)jiraHelper.getContextParams().get("issue");
        contextMap.put("customFieldManager", customFieldManager);
        contextMap.put("issueManager", issueManager);
        contextMap.put("issue", issue);
        contextMap.put("number", numberTool);
        contextMap.put("baseURL", getJiraURL());
        contextMap.put("supplierOffers", fieldGetter.getSupplierOffers(issue));
        contextMap.put("DMV", this);
        return contextMap;
    }

    private String getJiraURL() {
        return ComponentAccessor.getApplicationProperties().getString("jira.baseurl");
    }

}
