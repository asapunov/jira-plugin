package com.plugins.documentation;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DocumentationModuleValue extends AbstractJiraContextProvider {

    private static final Logger log = LoggerFactory.getLogger(IssueListener.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue issue = (Issue)jiraHelper.getContextParams().get("issue");
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject((long) 10000);
        String cfValue = customField != null ? customField.getValueFromIssue(issue) : null;
        String key = issue.getKey();
        log.warn(key);
        log.warn(cfValue);
        contextMap.put("issueKey", key);
        contextMap.put("value10000", cfValue);
        return contextMap;
    }

}
