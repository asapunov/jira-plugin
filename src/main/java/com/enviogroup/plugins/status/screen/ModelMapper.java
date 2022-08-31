package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.customfields.option.LazyLoadedOption;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.HashMap;
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
        /* № Процедуры */
        model.setProcedureNumber(issueWorker.getStringCustomFieldValue(CUSTOM_FIELD_10132, issue));
        /* Сумма закупки в руб */
        model.setSaleAmount(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10522, issue));
        /* Сумма нового предложения в руб*/
        Double newOffer = issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10056, issue);

        if (newOffer == null || newOffer.isNaN()) {
            /* Сумма КП в руб */
            model.setOffer(issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10518, issue));
        } else {
            /* Сумма нового предложения в руб */
            model.setOffer(newOffer);
        }
        for (Map.Entry entry : documentsMap.entrySet()) {
            Issue issueDoc = (MutableIssue) entry.getValue();
            AgreementModel agreementModel = new AgreementModel();
            OrganisationModel organisationModel = new OrganisationModel();
            Issue org = (issueWorker.getMutableIssuesList(issueDoc, CUSTOM_FIELD_10069)).get(0);
            organisationModel.setKey(org.getKey());
            LazyLoadedOption orgType = (LazyLoadedOption) issueWorker.getObjectCustomFieldValue(CUSTOM_FIELD_10029, org);
            organisationModel.setType(orgType.getValue());
            organisationModel.setName(org.getSummary());
            LazyLoadedOption orgStatus = (LazyLoadedOption) issueWorker.getObjectCustomFieldValue(CUSTOM_FIELD_10121, org);
            if(orgStatus != null) {
                String organisationStatus = orgStatus.getValue();
                organisationModel.setStatus(organisationStatus);
                if (ORGANISATION_STATUS_APPROVED.equals(organisationStatus)) {
                    organisationModel.setStatusColor(ORGANISATION_STATUS_APPROVED_STATUS);
                } else if (ORGANISATION_STATUS_NOT_APPROVED.equals(organisationStatus)){
                    organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_APPROVED_STATUS);
                } else if (ORGANISATION_STATUS_NOT_VELIDATED.equals(organisationStatus)) {
                    organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_VELIDATED_STATUS);
                } else {
                    organisationModel.setStatusColor(ORGANISATION_STATUS_DEFAULT_STATUS);
                }
            } else {
                organisationModel.setStatus(ORGANISATION_STATUS_NOT_SET);
                organisationModel.setStatusColor(ORGANISATION_STATUS_NOT_SET_STATUS);
            }
            agreementModel.setOrganisation(organisationModel);
            agreementModel.setAmount((issueWorker.getDoubleCustomFieldValue(CUSTOM_FIELD_10072, issueDoc)));
            agreementModel.setKey(issueDoc.getKey());
            agreementModel.setStatus(issueDoc.getStatus());
            agreementModel.setSummary(issueDoc.getSummary());
            if (organisationModel.getKey().equals(ORG_1)) {
                model.setAgreement(agreementModel);
            } else {
                model.addAgreement(agreementModel);
            }
        }
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("model", model);
        return modelMap;
    }
}