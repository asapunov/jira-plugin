package com.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.enviogroup.plugins.documentation.FieldGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ApprovalsActivity extends JiraWebActionSupport {

    private static final Logger log = LoggerFactory.getLogger(ApprovalsActivity.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();
    private static final CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
    private static final FieldGetter fieldGetter = new FieldGetter();
    private final JiraHelper jiraHelper = new JiraHelper(getHttpRequest());

    public String doDefault() {
        Issue issueObject = issueManager.getIssueObject("CRM-4");
        ArrayList<MutableIssue> issues = fieldGetter.getSupplierOffers(getIssue());
        for (MutableIssue i : issues) {

        }
        validation(issueObject, 51);
        return "updateIssue";
    }

    public Issue getIssue() {
        return (Issue) jiraHelper.getContextParams().get("issue");
    }

    public IssueService.IssueResult validation(Issue issue, int actionId) {
        IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
        IssueService.TransitionValidationResult transitionValidationResult = issueService.validateTransition(currentUser, issue.getId(), actionId, issueInputParameters);
        if (transitionValidationResult.isValid()) {
            IssueService.IssueResult transitionResult = issueService.transition(currentUser, transitionValidationResult);
            if (transitionResult.isValid())
                log.debug("Transitioned issue" + issue + "through action $actionId");
            else
                log.debug("Transition result is not valid");
            return transitionResult;
        } else {
            log.debug("The transitionValidation is not valid");
            return null;
        }
    }
}
