package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.status.Status;
import com.enviogroup.plugins.status.screen.iterfaces.IssueModelImpl;

import java.util.Objects;

public abstract class IssueModel implements IssueModelImpl {
    private String key;

    private String summary;

    private StatusModel status;

    public IssueModel(String key) {
        this.key = key;
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

    public void setStatus(Status status) {
        this.status = new StatusModel(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueModel that = (IssueModel) o;
        return key.equals(that.key) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, summary, status);
    }
}
