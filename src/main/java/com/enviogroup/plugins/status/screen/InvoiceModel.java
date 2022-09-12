package com.enviogroup.plugins.status.screen;

import com.enviogroup.plugins.accountCF.Carrier;

import java.util.Collection;

public class InvoiceModel extends IssueModel {
    private Double amount;
    private OrganisationModel invoiceProvider;
    private Collection<Carrier> detailedInformation;

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

    public Collection<Carrier> getDetailedInformation() {
        return detailedInformation;
    }

    public void setDetailedInformation(Collection<Carrier> detailedInformation) {
        this.detailedInformation = detailedInformation;
    }
}
