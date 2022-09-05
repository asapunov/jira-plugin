package com.enviogroup.plugins.status.screen;

import java.util.ArrayList;
import java.util.List;

public class TenderModel extends IssueModel {
    private String procedureNumber;
    private AgreementModel agreement;
    private Double offer;
    private Double saleAmount;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<AgreementModel> agreementsList = new ArrayList<>();

    public TenderModel() {
    }

    public AgreementModel getAgreement() {
        return agreement;
    }

    public void setAgreement(AgreementModel agreement) {
        this.agreement = agreement;
    }

    public List<AgreementModel> getAgreementsList() {
        return agreementsList;
    }

    public void setAgreementsList(List<AgreementModel> agreementsList) {
        this.agreementsList = agreementsList;
    }

    public String getProcedureNumber() {
        return procedureNumber;
    }

    public void setProcedureNumber(String procedureNumber) {
        this.procedureNumber = procedureNumber;
    }

    public void addAgreement(AgreementModel agreementModel) {
        agreementsList.add(agreementModel);
    }

    public Double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }

}
