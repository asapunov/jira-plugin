package com.enviogroup.plugins.status.screen;
import com.atlassian.jira.issue.status.Status;

public class AgreementModel {
    private String key;
    private String summary;
    private Double amount;
    private Status status;
    private OrganisationModel organisation;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OrganisationModel getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationModel organisation) {
        this.organisation = organisation;
    }

}
