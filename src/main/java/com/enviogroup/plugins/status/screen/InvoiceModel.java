package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.enviogroup.plugins.documentation.IssueWorker;

import static com.enviogroup.plugins.status.screen.CustomField.CUSTOM_FIELD_10107;
import static com.enviogroup.plugins.status.screen.CustomField.CUSTOM_FIELD_10108;

public class InvoiceModel extends IssueModel {
    private Double amount;
    private OrganisationModel invoiceProvider;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OrganisationModel getInvoiceProvider() {
        return invoiceProvider;
    }

    public void setInvoiceProvider(OrganisationModel invoiceProvider) {
        this.invoiceProvider = invoiceProvider;
    }
}
