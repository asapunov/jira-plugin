package com.enviogroup.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.customfields.option.LazyLoadedOption;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.enviogroup.plugins.status.screen.CustomField.CUSTOM_FIELD_10121;

public class IssueWorker {
    private  IssueManager issueManager = ComponentAccessor.getIssueManager();
    private  ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private  IssueService issueService = ComponentAccessor.getIssueService();
    private  CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

    private static final Logger log = LoggerFactory.getLogger(DocumentationModuleValue.class);

    public IssueWorker() {
    }
    public IssueWorker(IssueManager issueManager, ApplicationUser currentUser, IssueService issueService, CustomFieldManager customFieldManager) {
        this.issueManager = issueManager;
        this.currentUser = currentUser;
        this.issueService = issueService;
        this.customFieldManager = customFieldManager;

    }

    public ArrayList<MutableIssue> getMutableIssuesList(Issue issue, Long customFieldId) {
        String supplierOffers = getStringCustomFieldValue(customFieldId, issue);
        return getIssuesListFromStrings(supplierOffers);
    }

    private ArrayList<MutableIssue> getIssuesListFromStrings(String issueIdsListString) {
        ArrayList<MutableIssue> issuesSO = new ArrayList<>();
        if (issueIdsListString != null) {
            String[] offers = issueIdsListString.split(",");
            for (String issues : offers) {
                issues = issues.trim();
                issuesSO.add(issueManager.getIssueObject(issues));
            }
        }
        return issuesSO;
    }

    public ArrayList<MutableIssue> getMutableIssuesList(String issueId, Long customFieldId) {
        Issue issue = issueManager.getIssueObject(issueId);
        return getMutableIssuesList(issue, customFieldId);
    }

    public Issue getIssue(String issueId) {
        return issueManager.getIssueObject(issueId);
    }

    public String getStringCustomFieldValue(Long customFieldId, Issue issue) {
        CustomField customField = customFieldManager.getCustomFieldObject(customFieldId);
        return (customField != null ? (String) customField.getValue(issue) : null);
    }

    public Double getDoubleCustomFieldValue(Long customFieldId, Issue issue) {
        CustomField customField = customFieldManager.getCustomFieldObject(customFieldId);
        return (customField != null ? (Double) customField.getValue(issue) : null);
    }

    public Object getObjectCustomFieldValue(Long customFieldId, Issue issue) {
        CustomField customField = customFieldManager.getCustomFieldObject(customFieldId);
        return (customField != null ? customField.getValue(issue) : null);
    }

    public String getStringCustomFieldValue(Long customFieldId, String issueId) {
        Issue issue = issueManager.getIssueObject(issueId);
        return getStringCustomFieldValue(customFieldId, issue);
    }

    public String getStringValueFromLazyLoadedOptionCustomField(Long customFieldId, Issue issue) {
        LazyLoadedOption value = (LazyLoadedOption) getObjectCustomFieldValue(customFieldId, issue);
        if (value != null) {
            return value.getValue();
        } else {
            return null;
        }
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
                log.debug("Transitioned result is not valid");
            return transitionResult;
        } else {
            log.debug("The transitionValidation is not valid");
            return null;
        }
    }

    public Map<Integer, Object> issueMap(Issue issueId, Long customFieldId) {
        Map<Integer, Object> issueMap = new HashMap<>();
        int j = 0;
        for (MutableIssue i : getMutableIssuesList(issueId, customFieldId)) {
            issueMap.put(j++, i);
        }
        return issueMap;
    }

}
