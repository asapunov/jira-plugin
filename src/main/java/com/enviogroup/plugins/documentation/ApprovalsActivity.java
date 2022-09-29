package com.enviogroup.plugins.documentation;

import com.atlassian.jira.web.action.JiraWebActionSupport;

import static com.enviogroup.plugins.status.screen.CustomField.CUSTOM_FIELD_11411;

public class ApprovalsActivity extends JiraWebActionSupport {
    private String issueId;
    private String parentId;

//    @Override
//    public String doDefault() throws Exception {
//        return "view";
//    }

    @Override
    protected String doExecute() throws Exception {
        IssueWorker issueWorker = new IssueWorker();
        String issuesIds = issueWorker.getStringCustomFieldValue(CUSTOM_FIELD_11411, this.parentId);
        String[] issuesIdsArray = issuesIds.split(", ");
        issueWorker.approval(issueId, 111);
        if (issuesIdsArray.length > 1) {
            for (String i : issuesIdsArray) {
                if (!i.equals(issueId)) {
                    issueWorker.approval(i, 121);
                }
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

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public String getIssueId() {
        return this.issueId;
    }

}
