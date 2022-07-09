package com.enviogroup.plugins.admin;

import com.atlassian.jira.web.action.JiraWebActionSupport;


public class PluginAdminConfigAction extends JiraWebActionSupport {

    public static final String CONFIG_NAME = "com.enviogroup.config";
    private String config;


    public String doDefault() throws Exception {
        return INPUT;
    }

    protected String doExecute() throws Exception {
        return INPUT;
    }


}