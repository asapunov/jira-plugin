package com.enviogroup.plugins.status.screen;

import javax.xml.bind.annotation.XmlElement;

public abstract class IssueModel {
    @XmlElement(name = "key", required = true)
    private String key;

    @XmlElement(name = "summary")
    private String summary;

    @XmlElement(name = "status", required = true)
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

    public void setStatus(String status) {
        this.status = new StatusModel(status);
    }
}
