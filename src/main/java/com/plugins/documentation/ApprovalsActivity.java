package com.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.web.action.JiraWebActionSupportEvent;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class ApprovalsActivity extends JiraWebActionSupport {

    private  final JiraHelper jiraHelper = new JiraHelper(getHttpRequest());
    private static final Logger log = LoggerFactory.getLogger(DocumentationModuleValue.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();
    private static final CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
    private static final FieldGetter fieldGetter = new FieldGetter();

    public String doDefault(){
        Issue issueObject = issueManager.getIssueObject("CRM-4");
        ArrayList <MutableIssue> issues = fieldGetter.getSupplierOffers(getIssue());
        for (MutableIssue i: issues){

        }
        validation(issueObject, 51);
        return "updateIssue";
    }

    public Issue getIssue(){
        return  (Issue)jiraHelper.getContextParams().get("issue");
    }

    public IssueService.IssueResult validation(Issue issue, int actionId){
        IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
        IssueService.TransitionValidationResult transitionValidationResult = issueService.validateTransition(currentUser, issue.getId(), actionId, issueInputParameters);
        if (transitionValidationResult.isValid()) {
            IssueService.IssueResult transitionResult = issueService.transition(currentUser, transitionValidationResult);
            if (transitionResult.isValid())
                log.debug("Transitioned issue" + issue + "through action $actionId");
            else
                log.debug("Transition result is not valid");
            return transitionResult;
        }
        else {
            log.debug("The transitionValidation is not valid");
            return null;
        }
    }
}
