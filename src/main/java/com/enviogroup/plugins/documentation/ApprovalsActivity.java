package com.enviogroup.plugins.documentation;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class ApprovalsActivity extends JiraWebActionSupport {
    private String issue;
    private String newIssue;

//    @Override
//    public String doDefault() throws Exception {
//        return "view";
//    }

    @Override
    protected String doExecute() throws Exception {
        this.newIssue =  "It's " + issue;
        return "issues";
    }

    @Override
    protected void doValidation() {
        if (null == issue) {
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
}
