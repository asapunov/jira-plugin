package com.enviogroup.plugins.status.screen;

public abstract class IssueModel {
    private String key;

    private String summary;

    private StatusModel status;

    public IssueModel(String key) {
        this.key = key;
    }

    public IssueModel(StatusModel status) {
        this.status = status;
    }

    protected IssueModel() {
    }


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

    public StatusModel getStatus() {
        return status;
    }

    public void setStatus(StatusModel status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = new StatusModel(status);
    }
}
