package com.enviogroup.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApprovalsActivity extends JiraWebActionSupport {
    private String issue;
    private String newIssue;
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();

//    @Override
//    public String doDefault() throws Exception {
//        return "view";
//    }

    @Override
    protected String doExecute() throws Exception {
        Issue issueObject = issueManager.getIssueObject(issue);
        approval(issueObject, 61);
        this.newIssue =  issue + " approved";
        return "issues";
    }

    @Override
    protected void doValidation() {
        if (null == issue || issue.isEmpty()) {
            addErrorMessage("Пустое значение");
            return;
        }
        super.doValidation();
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNewIssue() {
        return this.newIssue;
    }

    private IssueService.IssueResult approval(Issue issue, int actionId) {
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
