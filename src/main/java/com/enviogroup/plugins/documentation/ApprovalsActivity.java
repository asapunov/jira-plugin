package com.enviogroup.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class ApprovalsActivity extends JiraWebActionSupport {
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();
    private String issueId;
    private String parentId;
    private String issuesIds;

//    @Override
//    public String doDefault() throws Exception {
//        return "view";
//    }

    @Override
    protected String doExecute() throws Exception {
        issuesIds = issuesIds.replace("[", "");
        issuesIds = issuesIds.replace("]", "");
        String[] issuesIdsArray = issuesIds.split(", ");
        approval(issueId, 61);
        if (issuesIdsArray.length > 1) {
            for (String i : issuesIdsArray) {
                if (!i.equals(issueId))
                    approval(i, 51);
            }
        }
        return getRedirect("/browse/" + this.parentId);
    }

    @Override
    protected void doValidation() {
        if (null == issueId || issueId.isEmpty()) {
            addErrorMessage("Пустое значение");
            return;
        }
        super.doValidation();
    }

    private IssueService.IssueResult approval(String issueId, int actionId) {
        Issue issue = issueManager.getIssueObject(issueId);
        issue.getStatus().getSimpleStatus();
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

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setIssuesIds(String issuesIds) {
        this.issuesIds = issuesIds;
    }

    public String getParentId() {
        return this.parentId;
    }

    public String getIssuesIds() {
        return this.issuesIds;
    }

}
