package com.plugins.documentation;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.velocity.NumberTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DocumentationModuleValue extends AbstractJiraContextProvider {

    private static final Logger log = LoggerFactory.getLogger(DocumentationModuleValue.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final NumberTool numberTool = new NumberTool(new Locale("ru", "RU"));
    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue issue = (Issue)jiraHelper.getContextParams().get("issue");
        contextMap.put("customFieldManager", ComponentAccessor.getCustomFieldManager());
        contextMap.put("issueManager", issueManager);
        contextMap.put("issue", issue);
        contextMap.put("number", numberTool);
        contextMap.put("baseURL", getjiraURL());
        return contextMap;
    }

/*    private String getCfValue(String cfNumber, Issue issue) {
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(cfNumber);
        try {
            String cfValue = (customField != null ? customField.getValue(issue).toString() : null);
            log.warn(cfValue);
            return cfValue;
        }
        catch (Exception e) {
            log.warn("Field is empty");
            return null;
        }
    }*/

    private String getjiraURL() {
        return ComponentAccessor.getApplicationProperties().getString("jira.baseurl");
    }

}
