package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.status.Status;

public abstract class IssueModel {
    private String key;
    private String summary;
    private Status status;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
