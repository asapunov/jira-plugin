package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.atlassian.jira.issue.fields.screen.FieldScreenManager;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.velocity.NumberTool;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatusScreen extends AbstractJiraContextProvider {

    @ComponentImport
    private final FieldScreenManager fieldScreenManager;
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();
    private static final CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
    private static final NumberTool numberTool = new NumberTool(new Locale("ru", "RU"));
    private static final IssueWorker issueWorker = new IssueWorker(currentUser, issueService, customFieldManager, issueManager);

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
        contextMap.put("issueManager", issueManager);
        contextMap.put("currentUser", currentUser);
        contextMap.put("issueService", issueService);
        contextMap.put("customFieldManager", customFieldManager);
        contextMap.put("number", numberTool);
        contextMap.put("issue", issue);
        contextMap.put("soyRenderer", ComponentAccessor.getComponent(SoyTemplateRendererProvider.class).getRenderer());
        contextMap.put("documents", issueWorker.getMutableIssuesList(issue, "Договоры"));
        return contextMap;
    }
}
