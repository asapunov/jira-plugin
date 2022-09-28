package com.enviogroup.plugins.status.screen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class AgreementModel extends IssueModel {
    private Double amount;
    private OrganisationModel organisation;
    private Double valueAddedTax;
    private List<InvoiceModel> inputInvoicesList = new ArrayList<>();
    private List<SpecificationModel> specificationsList = new ArrayList<>();

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

    public Double getValueAddedTax() {
        return valueAddedTax;
    }

    public void setValueAddedTax(Double valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public List<SpecificationModel> getSpecificationsList() {
        return specificationsList;
    }

    public void setSpecificationsList(List<SpecificationModel> specificationsList) {
        this.specificationsList = specificationsList;
    }

    public void addSpecification(SpecificationModel specification) {
        specificationsList.add(specification);
    }

    public List<InvoiceModel> getInputInvoicesList() {
        return inputInvoicesList;
    }

    public void setInputInvoicesList(List<InvoiceModel> inputInvoices) {
        this.inputInvoicesList = inputInvoices;
    }

    public void addInputInvoice(InvoiceModel inputInvoice) {
        inputInvoicesList.add(inputInvoice);
    }

}
