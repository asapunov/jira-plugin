package com.enviogroup.plugins.panel.tender;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.user.ApplicationUser;

import java.util.HashMap;
import java.util.Map;

import static com.enviogroup.plugins.status.screen.CustomField.CUSTOM_FIELD_11411;

public class RightPanel extends AbstractJiraContextProvider {
    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue issue = (Issue) jiraHelper.getContextParams().get("issue");
        contextMap.put("issue", issue);
        contextMap.put("baseURL", getJiraURL());
        contextMap.put("soyRenderer", ComponentAccessor.getComponent(SoyTemplateRendererProvider.class).getRenderer());
        return contextMap;
    }

    private String getJiraURL() {
        return ComponentAccessor.getApplicationProperties().getString("jira.baseurl");
    }
}
