package com.enviogroup.plugins.status.screen;

import java.util.List;

public class SpecificationModel extends IssueModel {
    private Double amount;
    private List<InvoiceModel> invoiceModelList;

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
}
