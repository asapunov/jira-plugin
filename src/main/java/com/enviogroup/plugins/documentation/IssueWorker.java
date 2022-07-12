package com.enviogroup.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class IssueWorker {
    private static final Logger log = LoggerFactory.getLogger(DocumentationModuleValue.class);
    private final IssueManager issueManager;
    private final CustomFieldManager customFieldManager;
    private final IssueService issueService;
    private final ApplicationUser currentUser;

    public IssueWorker(ApplicationUser currentUser, IssueService issueService, CustomFieldManager customFieldManager, IssueManager issueManager) {
        this.currentUser = currentUser;
        this.issueService = issueService;
        this.customFieldManager = customFieldManager;
        this.issueManager = issueManager;
    }

    public ArrayList<MutableIssue> getMutableIssuesList(Issue issue, String cfName) {
        String supplierOffers = getCfValue(cfName, issue);
        ArrayList<MutableIssue> issuesSO = new ArrayList<>();
        if (supplierOffers != null) {
            String[] offers = supplierOffers.split(",");
            for (String issues : offers) {
                issues = issues.trim();
                issuesSO.add(issueManager.getIssueObject(issues));
            }
        }
        return issuesSO;
    }

    public ArrayList<MutableIssue> getMutableIssuesList(String issueId, String cfName) {
        Issue issue = issueManager.getIssueObject(issueId);
        return getMutableIssuesList(issue, cfName);
    }

    public String getCfValue(String cfName, Issue issue) {
        CustomField customField = customFieldManager.getCustomFieldObjectByName(cfName);
        try {
            String cfValue = (customField != null ? customField.getValue(issue).toString() : null);
            log.warn(cfValue);
            return cfValue;
        } catch (Exception e) {
            log.warn("Field is empty");
            return null;
        }
    }

    public String getCfValue(String cfName, String issueId) {
        Issue issue = issueManager.getIssueObject(issueId);
        return getCfValue(cfName, issue);
    }

    public IssueService.IssueResult approval(String issueId, int actionId) {
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


}
