package com.enviogroup.plugins.status.screen;

import com.enviogroup.plugins.accountCF.Carrier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Collection;

@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceModel extends IssueModel {
    private Double amount;
    private OrganisationModel organisation;
    private OrganisationModel invoiceProvider;
    private Collection<Carrier> detailedInformation;
    private String type;

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

    public OrganisationModel getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationModel organisation) {
        this.organisation = organisation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
