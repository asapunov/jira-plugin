package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.enviogroup.plugins.accountCF.Carrier;
import com.enviogroup.plugins.documentation.IssueWorker;
import com.enviogroup.plugins.status.screen.letters.BaseLetterModel;
import com.enviogroup.plugins.status.screen.letters.InputLetterModel;
import com.enviogroup.plugins.status.screen.letters.OutputLetterModel;

import java.sql.Timestamp;
import java.util.*;

import static com.enviogroup.plugins.status.screen.CustomField.*;

public class ModelMapper {
    private final IssueWorker issueWorker = new IssueWorker();

    public ModelMapper() {
    }

    public TenderModel getModel(String issueId) {
        Issue issue = issueWorker.getIssue(issueId);
        return getModel(issue);
    }

    public TenderModel getModel(Issue issue) {
        if (issue == null) {
            return new TenderModel();
        }
        TenderModel model = new TenderModel();
        model.setKey(issue.getKey());
        model.setSummary(issue.getSummary());
        model.setStatus(issue.getStatus());
        model.setProcedureNumber(issueWorker.getStringCustomFieldValue(CUSTOM_FIELD_10132, issue));
        model.setBuyAmount(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10522, issue));
        Double newSaleAmount = issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10056, issue);
        model.setUrl(issueWorker.getStringCustomFieldValue(CUSTOM_FIELD_10047, issue));
        model.setInvoicesList(invoiceModelListFactory(issue, issueWorker, CUSTOM_FIELD_10229));
        if (newSaleAmount == null || newSaleAmount.isNaN()) {
            model.setSaleAmount(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10518, issue));
        } else {
            model.setSaleAmount(newSaleAmount);
        }
        model.setFinanceModel(financeModelFactory(issue, issueWorker));

        Map<Integer, Object> documentsMap = issueWorker.issueMap(issue, CUSTOM_FIELD_10327);
        for (Map.Entry entry : documentsMap.entrySet()) {
            Issue issueDoc = (MutableIssue) entry.getValue();
            if (!issueDoc.getIssueTypeId().equals(Long.toString(ISSUE_TYPE_ID_DOGOVOR))) {
                continue;
            }
            AgreementModel agreementModel = new AgreementModel();
            Issue org = (issueWorker.getMutableIssuesList(issueDoc, CUSTOM_FIELD_10069)).get(0);
            if (isOrgOurs(org)) {
                org = (issueWorker.getMutableIssuesList(issueDoc, CUSTOM_FIELD_10067)).get(0);
                model.setAgreement(agreementModel);
            } else {
                model.addAgreement(agreementModel);
            }
            OrganisationModel organisationModel = organisationModelFactory(org, issueWorker);
            String vatString = issueWorker.getStringValueFromLazyLoadedOptionCustomField(CUSTOM_FIELD_10113, issueDoc);
            double vatDouble;
            try {
                vatDouble = Double.parseDouble(vatString) / 100;
            } catch (Exception e) {
                vatDouble = 0D;
            }
            agreementModel.setValueAddedTax(vatDouble);
            agreementModel.setOrganisation(organisationModel);
            agreementModel.setAmount((issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10072, issueDoc)));
            agreementModel.setKey(issueDoc.getKey());
            agreementModel.setStatus(issueDoc.getStatus());
            agreementModel.setSummary(issueDoc.getSummary());
            agreementModel.setSpecificationsList(specificationModelListFactory(issueDoc, issueWorker));
            agreementModel.setInputInvoicesList(invoiceModelListFactory(issueDoc, issueWorker, CUSTOM_FIELD_11201));
            setAgreementAlarm(agreementModel);
            agreementModel.setShipmentsList(shipmentModelListFactory(issueDoc, issueWorker));
        }
        editModelAmountWithVAT(model);
        return model;
    }

//    public List<BaseLetterModel> lettersModelListFactory(Issue issue, IssueWorker issueWorker, Long cfId) {
//        List<BaseLetterModel> lettersList = new ArrayList<>();
//        if (!issueWorker.getMutableIssuesList(issue, cfId).isEmpty()) {
//            for (Issue letterIssue : (issueWorker.getMutableIssuesList(issue, cfId))) {
//                String letterTypeId = Objects.requireNonNull(letterIssue.getIssueType()).getId();
//                if (letterTypeId.equals(INPUT_LETTER_ISSUE_TYPE_ID)) {
//                    InputLetterModel inputLetter = inputLetterModelFactory(letterIssue, 0);
//                    if (inputLetter != null) {
//                        lettersList.add(inputLetter);
//                    }
//                } else if (letterTypeId.equals(OUTPUT_LETTER_ISSUE_TYPE_ID)) {
//                    OutputLetterModel outputLetter = outputLetterModelFactory(letterIssue, 0);
//                    if (outputLetter != null) {
//                        lettersList.add(outputLetter);
//                    }
//                }
//            }
//            return lettersList;
//        } else {
//            return null;
//        }
//    }
//
//    private OutputLetterModel outputLetterModelFactory(Issue issue, int i) {
//        OutputLetterModel outputLetter = new InputLetterModel();
//        outputLetter.setKey(issue.getKey());
//        outputLetter.setStatus(issue.getStatus());
//        outputLetter.setSummary(issue.getSummary());
//        //outputLetter.setChildInputLetter(inputLetterModelFactory(issueWorker.getMutableIssuesList(issue, CUSTOM_FIELD_12300).get(0)));
//        i++;
//        outputLetter.setParentInputLetter(inputLetterModelFactory(issueWorker.getMutableIssuesList(issue, CUSTOM_FIELD_10541).get(0), i));
//        if (outputLetter.getParentInputLetter() == null) {
//            return outputLetter;
//        } else {
//            return null;
//        }
//    }
//
//    private InputLetterModel inputLetterModelFactory(Issue issue, int i) {
//        //TODO: Пока не придумал как остановить если сообщения замкнуться в круг
//        if (i > 20) {
//            return null;
//        }
//        InputLetterModel inputLetter = new InputLetterModel();
//        inputLetter.setKey(issue.getKey());
//        inputLetter.setStatus(issue.getStatus());
//        inputLetter.setSummary(issue.getSummary());
//        //inputLetter.setChildOutputLetter(outputLetterModelFactory(issueWorker.getMutableIssuesList(issue, CUSTOM_FIELD_10542).get(0)));
//        i++;
//        inputLetter.setParentOutputLetter(outputLetterModelFactory(issueWorker.getMutableIssuesList(issue, CUSTOM_FIELD_12301).get(0), i));
//        if (inputLetter.getParentOutputLetter() == null) {
//            return inputLetter;
//        } else {
//            return null;
//        }
//    }

    public List<InvoiceModel> invoiceModelListFactory(Issue issue, IssueWorker issueWorker, Long cfId) {
        List<InvoiceModel> invoiceList = new ArrayList<>();
        if (!issueWorker.getMutableIssuesList(issue, cfId).isEmpty()) {
            for (Issue invoiceIssue : (issueWorker.getMutableIssuesList(issue, cfId))) {
                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setKey(invoiceIssue.getKey());
                invoiceModel.setStatus(invoiceIssue.getStatus());
                invoiceModel.setSummary(invoiceIssue.getSummary());
                invoiceModel.setAmount((issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10085, invoiceIssue)));
                invoiceList.add(invoiceModel);
                invoiceModel.setDetailedInformation((Collection<Carrier>) issueWorker.getObjectCustomFieldValue(CUSTOM_FIELD_11300, invoiceIssue));
                invoiceModel.setType(issueWorker.getStringValueFromLazyLoadedOptionCustomField(CUSTOM_FIELD_10543, invoiceIssue));
                try {
                    Issue org = issueWorker.getMutableIssuesList(invoiceIssue, CUSTOM_FIELD_10339).get(0);
                    invoiceModel.setOrganisation(organisationModelFactory(org, issueWorker));
                } catch (Exception e) {
                    invoiceModel.setOrganisation(null);
                }
            }
            return invoiceList;
        } else {
            return null;
        }
    }

    public List<SpecificationModel> specificationModelListFactory(Issue agreementIssue, IssueWorker issueWorker) {
        List<SpecificationModel> specificationList = new ArrayList<>();
        if (!issueWorker.getMutableIssuesList(agreementIssue, CUSTOM_FIELD_10107).isEmpty()) {
            for (Issue specificationIssue : (issueWorker.getMutableIssuesList(agreementIssue, CUSTOM_FIELD_10107))) {
                SpecificationModel specificationModel = new SpecificationModel();
                specificationModel.setKey(specificationIssue.getKey());
                specificationModel.setStatus(specificationIssue.getStatus());
                specificationModel.setSummary(specificationIssue.getSummary());
                specificationModel.setAmount((issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10108, specificationIssue)));
                specificationModel.setInvoiceModelList(invoiceModelListFactory(specificationIssue, issueWorker, CUSTOM_FIELD_10356));
                specificationModel.setDetailedInformation((Collection<Carrier>) issueWorker.getObjectCustomFieldValue(CUSTOM_FIELD_12500, specificationIssue));
                specificationModel.setDeliveryTime((Timestamp) issueWorker.getObjectCustomFieldValue(CUSTOM_FIELD_10112, specificationIssue));
                try {
                    Issue org = isOrgOurs((issueWorker.getMutableIssuesList(specificationIssue, CUSTOM_FIELD_10341)).get(0)) ? issueWorker.getMutableIssuesList(specificationIssue, CUSTOM_FIELD_10343).get(0) : issueWorker.getMutableIssuesList(specificationIssue, CUSTOM_FIELD_10341).get(0);
                    specificationModel.setOrganisation(organisationModelFactory(org, issueWorker));
                } catch (Exception e) {
                    specificationModel.setOrganisation(null);
                }
                specificationList.add(specificationModel);
            }
            return specificationList;
        } else {
            return null;
        }
    }

    public List<ShipmentModel> shipmentModelListFactory(Issue agreementIssue, IssueWorker issueWorker) {
        List<ShipmentModel> shipmentModelList = new ArrayList<>();
        if (!issueWorker.getMutableIssuesList(agreementIssue, CUSTOM_FIELD_10099).isEmpty()) {
            for (Issue shipmentIssue : (issueWorker.getMutableIssuesList(agreementIssue, CUSTOM_FIELD_10099))) {
                ShipmentModel shipmentModel = new ShipmentModel();
                shipmentModel.setKey(shipmentIssue.getKey());
                shipmentModel.setStatus(shipmentIssue.getStatus());
                shipmentModel.setSummary(shipmentIssue.getSummary());
                try {
                    Issue org = isOrgOurs((issueWorker.getMutableIssuesList(shipmentIssue, CUSTOM_FIELD_10346)).get(0)) ? issueWorker.getMutableIssuesList(shipmentIssue, CUSTOM_FIELD_10348).get(0) : issueWorker.getMutableIssuesList(shipmentIssue, CUSTOM_FIELD_10346).get(0);
                    shipmentModel.setOrganisation(organisationModelFactory(org, issueWorker));
                } catch (Exception e) {
                    shipmentModel.setOrganisation(null);
                }
                shipmentModelList.add(shipmentModel);
            }
            return shipmentModelList;
        } else {
            return null;
        }
    }

    public OrganisationModel organisationModelFactory(Issue orgIssue, IssueWorker issueWorker) {
        OrganisationModel organisationModel = new OrganisationModel();
        organisationModel.setKey(orgIssue.getKey());
        organisationModel.setSummary(orgIssue.getSummary());
        String orgStatus = issueWorker.getStringValueFromLazyLoadedOptionCustomField(CUSTOM_FIELD_10121, orgIssue);
        if (orgStatus != null) {
            organisationModel.setOrgStatus(orgStatus);
            switch (orgStatus) {
                case ORGANISATION_STATUS_APPROVED:
                    organisationModel.setStatusColor(ORGANISATION_STATUS_APPROVED_STATUS);
                    break;
                case ORGANISATION_STATUS_NOT_APPROVED:
                    organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_APPROVED_STATUS);
                    break;
                case ORGANISATION_STATUS_NOT_VELIDATED:
                    organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_VELIDATED_STATUS);
                    break;
                default:
                    organisationModel.setStatusColor(ORGANISATION_STATUS_DEFAULT_STATUS);
                    break;
            }
        } else {
            organisationModel.setOrgStatus(ORGANISATION_STATUS_NOT_SET);
            organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_SET_STATUS);
        }
        return organisationModel;
    }

    public FinanceModel financeModelFactory(Issue tenderIssue, IssueWorker issueWorker) {
        FinanceModel financeModel = new FinanceModel();
        financeModel.setAdditionalExpenses(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_12101, tenderIssue));
        financeModel.setFinanceExpenses(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10515, tenderIssue));
        financeModel.setLogisticExpenses(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10513, tenderIssue));
        financeModel.setTravelExpenses(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10514, tenderIssue));
        return financeModel;
    }


    private boolean isOrgOurs(Issue org) {
        return org.getKey().equals(ORG_1) || org.getKey().equals(ORG_2365);
    }

    private void editModelAmountWithVAT(TenderModel model) {
        if (model.getAgreement() != null && model.getAgreement().getValueAddedTax() != 0) {
            Double agreementAmount = model.getAgreement().getAmount();
            model.getAgreement().setAmount(agreementAmount * VAT_COEFFICIENT);
            if (model.getAgreement().getSpecificationsList() != null) {
                for (SpecificationModel sp : model.getAgreement().getSpecificationsList()) {
                    Double specificationAmount = sp.getAmount();
                    if (specificationAmount != null) {
                        sp.setAmount(specificationAmount * VAT_COEFFICIENT);
                    }
                }
            }
        }
        for (AgreementModel am : model.getAgreementsList()) {
            if (am.getValueAddedTax() != 0) {
                Double agreementAmount = am.getAmount();
                if (agreementAmount != null) {
                    am.setAmount(agreementAmount * VAT_COEFFICIENT);
                }
                if (am.getSpecificationsList() != null) {
                    for (SpecificationModel sp : am.getSpecificationsList()) {
                        Double specificationAmount = sp.getAmount();
                        if (specificationAmount != null) {
                            sp.setAmount(specificationAmount * VAT_COEFFICIENT);
                        }
                    }
                }
            }
        }
//        if (am.getInputInvoicesList() != null) {
//            for (InvoiceModel in : am.getInputInvoicesList()) {
//                amount = in.getAmount();
//                if (amount != null) {
//                    in.setAmount(amount * VAT_COEFFICIENT);
//                }
//            }
//        }
    }

    private void setAgreementAlarm(AgreementModel agreementModel) {
        agreementModel.setAlarm(false);
        try {
            if (agreementModel.getInputInvoicesList() != null && agreementModel.getSpecificationsList() != null) {
                for (SpecificationModel specification : agreementModel.getSpecificationsList()) {
                    for (InvoiceModel invoice : agreementModel.getInputInvoicesList()) {
                        if (specification.getOrganisation().equals(invoice.getOrganisation())) {
                            agreementModel.setAlarm(true);
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }
}

