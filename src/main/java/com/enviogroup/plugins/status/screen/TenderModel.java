package com.enviogroup.plugins.status.screen;

import java.util.ArrayList;
import java.util.List;

public class TenderModel {
    private String key;
    private String procedureNumber;
    private AgreementModel agreement;
    private Double offer;
    private Double offer2;
    private Double saleAmount;
    private Double margin = getBuyAmount() - saleAmount;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setSaleAmount(java.lang.Double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Double getBuyAmount() {
        if (offer2 == null)
            return offer;
        else
            return offer2;
    }

    public Double getMargin() {
        return margin;
    }

    public Double getMarginPercent() {
        if (getBuyAmount() != 0 || getBuyAmount() != null)
            return  getMargin() / getBuyAmount() * 100;
        else
            return null;
    }

    public void setOffer(java.lang.Double offer) {
        this.offer = offer;
    }

    public void setOffer2(java.lang.Double offer2) {
        this.offer2 = offer2;
    }
}
