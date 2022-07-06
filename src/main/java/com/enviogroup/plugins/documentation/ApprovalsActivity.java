package com.enviogroup.plugins.documentation;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public class ApprovalsActivity extends JiraWebActionSupport {
    private String string;
    private String string2;

    @Override
    public String doDefault() throws Exception {
        return "view";
    }

    @Override
    protected String doExecute() throws Exception {
        string2 = "You  entered " + string;
        return "view";
    }

    @Override
    protected void doValidation() {
        if (null == string || string.isEmpty()) {
            addErrorMessage("Пустое значение");
            return;
        }
        super.doValidation();
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString2() {
        return string2;
    }
}
