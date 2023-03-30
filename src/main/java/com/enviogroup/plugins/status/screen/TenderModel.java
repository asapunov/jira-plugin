package com.enviogroup.plugins.status.screen;

import com.enviogroup.plugins.status.screen.letters.BaseLetterModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class TenderModel extends IssueModel {
    private String procedureNumber;
    private AgreementModel agreement;
    private Double saleAmount;
    private Double buyAmount;
    private String url;
    private FinanceModel financeModel;
    private List<AgreementModel> agreementsList = new ArrayList<>();
    private List<InvoiceModel> invoicesList = new ArrayList<>();

    private List<BaseLetterModel> lettersList = new ArrayList<>();

    public TenderModel(String key) {
        super(key);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public Double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(Double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public Double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public FinanceModel getFinanceModel() {
        return financeModel;
    }

    public void setFinanceModel(FinanceModel financeModel) {
        this.financeModel = financeModel;
    }

    public List<InvoiceModel> getInvoicesList() {
        return invoicesList;
    }

    public void setInvoicesList(List<InvoiceModel> invoicesList) {
        this.invoicesList = invoicesList;
    }

    public List<BaseLetterModel> getLettersList() {
        return lettersList;
    }

    public void setLettersList(List<BaseLetterModel> lettersList) {
        this.lettersList = lettersList;
    }

    public void addLetter(BaseLetterModel letter){
        this.lettersList.add(letter);
    }
}
