package com.enviogroup.plugins.status.screen;

import com.enviogroup.plugins.accountCF.Carrier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SpecificationModel extends IssueModel {
    private Double amount;
    private OrganisationModel organisation;
    private List<InvoiceModel> invoiceModelList;
    private Collection<Carrier> detailedInformation;
    private Timestamp deliveryTime;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<InvoiceModel> getInvoiceModelList() {
        return invoiceModelList;
    }

    public void setInvoiceModelList(List<InvoiceModel> invoiceModelList) {
        this.invoiceModelList = invoiceModelList;
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

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
