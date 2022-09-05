package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.enviogroup.plugins.status.screen.CustomField.*;

public class ModelMapper {
    private final Map entityConverterMap;
    private final IssueWorker issueWorker;
    private final Issue issue;
    private final Map<Integer, Object> documentsMap;


    public ModelMapper(JiraHelper jiraHelper, EntityConverter entityConverter) {
        this.entityConverterMap = entityConverter.getEntityConverterMap(jiraHelper);
        this.issueWorker = (IssueWorker) entityConverterMap.get("issueWorker");
        this.issue = (Issue) entityConverterMap.get("issue");
        this.documentsMap = (Map) entityConverterMap.get("documents");
    }

    public Map<String, Object> getModelMap() {
        TenderModel model = new TenderModel();
        model.setKey(issue.getKey());
        model.setProcedureNumber(issueWorker.getStringCustomFieldValue(CUSTOM_FIELD_10132, issue));
        model.setSaleAmount(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10522, issue));
        Double newOffer = issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10056, issue);
        model.setUrl(issueWorker.getStringCustomFieldValue(CUSTOM_FIELD_10047, issue));
        if (newOffer == null || newOffer.isNaN()) {
            model.setOffer(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10518, issue));
        } else {
            model.setOffer(newOffer);
        }
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
            agreementModel.setInputInvoicesList(invoiceModelListFactory(issueDoc, issueWorker));
        }
        if (model.getAgreement() != null && model.getAgreement().getValueAddedTax() == 0) {
            for (AgreementModel am : model.getAgreementsList()) {
                Double amount = am.getAmount();
                if (amount != null) {
                    am.setAmount(amount - amount * am.getValueAddedTax());
                }
            }
        }
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("model", model);
        return modelMap;
    }

    public List<InvoiceModel> invoiceModelListFactory(Issue agreementIssue, IssueWorker issueWorker) {
        List<InvoiceModel> invoiceList = new ArrayList<>();
        if (!issueWorker.getMutableIssuesList(agreementIssue, CUSTOM_FIELD_11201).isEmpty()) {
            for (Issue invoiceIssue : (issueWorker.getMutableIssuesList(agreementIssue, CUSTOM_FIELD_11201))) {
                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setKey(invoiceIssue.getKey());
                invoiceModel.setStatus(invoiceIssue.getStatus());
                invoiceModel.setSummary(invoiceIssue.getSummary());
                invoiceModel.setAmount((issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10085, invoiceIssue)));
                invoiceList.add(invoiceModel);
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

}