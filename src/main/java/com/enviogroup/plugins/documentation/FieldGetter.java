package com.enviogroup.plugins.documentation;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FieldGetter {
    private static final Logger log = LoggerFactory.getLogger(DocumentationModuleValue.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private static final ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    private static final IssueService issueService = ComponentAccessor.getIssueService();
    private static final CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

    public ArrayList<MutableIssue> getSupplierOffers(Issue issue) {
        String supplierOffers = getCfValue("Предложения поставщиков", issue);
        assert supplierOffers != null;
        String[] offers = supplierOffers.split(",");
        ArrayList<MutableIssue> issuesSO = new ArrayList<>();
        for (String issues : offers) {
            issues = issues.trim();
            issuesSO.add(issueManager.getIssueObject(issues));
        }
        return issuesSO;
    }


    private String getCfValue(String cfName, Issue issue) {
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
}
