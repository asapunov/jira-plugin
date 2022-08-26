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
        //TODO:X
        model.setOffer(Double.parseDouble(issueWorker.getCfValue("Сумма КП в руб", issue)));

        model.setSaleAmount(Double.parseDouble(issueWorker.getCfValue("Сумма закупки в руб", issue)));
        for (Map.Entry entry : documentsMap.entrySet()) {

            Issue issue = (MutableIssue) entry.getValue();
            AgreementModel agreementModel = new AgreementModel();
            OrganisationModel organisationModel = new OrganisationModel();
            Issue org = (issueWorker.getMutableIssuesList(issue, "Поставщик")).get(0);
            organisationModel.setKey(org.getKey());
            organisationModel.setType(issueWorker.getCfValue("Тип организации", org));
            agreementModel.setOrganisation(organisationModel);
            agreementModel.setAmount(Double.parseDouble(issueWorker.getCfValue("Тип организации", org)));
            agreementModel.setKey(issue.getKey());
            agreementModel.setStatus(issue.getStatus().toString());
            agreementModel.setSummary(issue.getSummary());
            //TODO:X
            if (true /** issue.getType().equals("Поставщик")*/ ) {
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
