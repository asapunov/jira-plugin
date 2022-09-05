package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.enviogroup.plugins.documentation.IssueWorker;

import static com.enviogroup.plugins.status.screen.CustomField.*;

public class OrganisationModel extends IssueModel {
    private String type;
    private String orgStatus;
    private String statusColor;

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(String status) {
        this.orgStatus = status;
    }
}
