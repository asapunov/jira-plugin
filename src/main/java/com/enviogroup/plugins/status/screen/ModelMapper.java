package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.enviogroup.plugins.accountCF.Carrier;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
            if (org.getKey().equals(ORG_1)) {
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
        }
        if (model.getAgreement() != null && model.getAgreement().getValueAddedTax() == 0) {
            for (AgreementModel am : model.getAgreementsList()) {
                Double amount = am.getAmount();
                if (amount != null) {
                    am.setAmount(amount - amount * am.getValueAddedTax());
                }
            }
        }
        return model;
    }

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
                specificationList.add(specificationModel);
                specificationModel.setInvoiceModelList(invoiceModelListFactory(specificationIssue, issueWorker, CUSTOM_FIELD_10356));
                specificationModel.setDetailedInformation((Collection<Carrier>) issueWorker.getObjectCustomFieldValue(CUSTOM_FIELD_12500, specificationIssue));
            }
            return specificationList;
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
            if (ORGANISATION_STATUS_APPROVED.equals(orgStatus)) {
                organisationModel.setStatusColor(ORGANISATION_STATUS_APPROVED_STATUS);
            } else if (ORGANISATION_STATUS_NOT_APPROVED.equals(orgStatus)) {
                organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_APPROVED_STATUS);
            } else if (ORGANISATION_STATUS_NOT_VELIDATED.equals(orgStatus)) {
                organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_VELIDATED_STATUS);
            } else {
                organisationModel.setStatusColor(ORGANISATION_STATUS_DEFAULT_STATUS);
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

}