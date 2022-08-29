package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.HashMap;
import java.util.Map;

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
        model.setProcedureNumber(issueWorker.getCfValue("№ Процедуры", issue));
        model.setKey(issue.getKey());
        model.setProcedureNumber(issueWorker.getCfValue("№ Процедуры", issue));
        model.setSaleAmount(Double.parseDouble(issueWorker.getCfValue("Сумма закупки в руб", issue)));
        String newOffer = (issueWorker.getCfValue("Сумма нового предложения в руб", issue));
        if (newOffer == null || newOffer.isEmpty()) {
            model.setOffer(Double.parseDouble(issueWorker.getCfValue("Сумма КП в руб", issue)));
        } else {
            model.setOffer(Double.parseDouble(issueWorker.getCfValue("Сумма нового предложения в руб", issue)));
        }
        model.setSaleAmount(Double.parseDouble(issueWorker.getCfValue("Сумма закупки в руб", issue)));
        for (Map.Entry entry : documentsMap.entrySet()) {
            Issue issueDoc = (MutableIssue) entry.getValue();
            AgreementModel agreementModel = new AgreementModel();
            OrganisationModel organisationModel = new OrganisationModel();
            Issue org = (issueWorker.getMutableIssuesList(issueDoc, "Поставщик")).get(0);
            organisationModel.setKey(org.getKey());
            organisationModel.setType(issueWorker.getCfValue("Тип организации", org));
            agreementModel.setOrganisation(organisationModel);
            agreementModel.setAmount(Double.parseDouble(issueWorker.getCfValue("Сумма договора в руб с НДС", issueDoc)));
            agreementModel.setKey(issueDoc.getKey());
            agreementModel.setStatus(issueDoc.getStatus());
            agreementModel.setSummary(issueDoc.getSummary());
            if (organisationModel.getKey().equals("ORG-1")) {
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