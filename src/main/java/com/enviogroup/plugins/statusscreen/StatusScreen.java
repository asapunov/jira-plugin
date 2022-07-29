package com.enviogroup.plugins.statusscreen;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.atlassian.jira.issue.fields.screen.FieldScreenManager;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import java.util.HashMap;
import java.util.Map;

public class StatusScreen extends AbstractJiraContextProvider {

    @ComponentImport
    private final FieldScreenManager fieldScreenManager;

    public StatusScreen(FieldScreenManager fieldScreenManager) {
        this.fieldScreenManager = fieldScreenManager;
    }

    private FieldScreen getFieldScreen(int a) {
        Long aL = (long) a;
        return fieldScreenManager.getFieldScreen(aL);
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue issue = (Issue) jiraHelper.getContextParams().get("issue");
        FieldScreen fieldScreen = getFieldScreen(10200);
        contextMap.put("fieldScreen", fieldScreen);
        contextMap.put("soyRenderer", ComponentAccessor.getComponent(SoyTemplateRendererProvider.class).getRenderer());
        return contextMap;
    }
}
