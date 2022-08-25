package com.enviogroup.plugins.status.screen;

import java.util.ArrayList;
import java.util.List;

public class TenderModel {
    private String key;
    private String procedureNumber;
    private AgreementModel agreement;
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
}
