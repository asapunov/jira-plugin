package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        model.setOffer(Double.parseDouble(issueWorker.getCfValue("Сумма КП в руб", issue)));
        model.setOffer2(Double.parseDouble(issueWorker.getCfValue("Сумма нового предложения в руб", issue)));
        model.setSaleAmount(Double.parseDouble(issueWorker.getCfValue("Сумма закупки в руб", issue)));
        for (Map.Entry entry : documentsMap.entrySet()) {
            AgreementModel agreementModel = new AgreementModel();
            OrganisationModel organisationModel = new OrganisationModel();
            Issue org = (issueWorker.getMutableIssuesList(((MutableIssue) entry.getValue()), "Поставщик")).get(0);
            organisationModel.setKey(org.getKey());
            organisationModel.setType(issueWorker.getCfValue("Тип организации", org));
            agreementModel.setOrganisation(organisationModel);
            agreementModel.setAmount(Double.parseDouble(issueWorker.getCfValue("Тип организации", org)));
            agreementModel.setKey(((MutableIssue) entry.getValue()).getKey());
            agreementModel.setStatus(((MutableIssue) entry.getValue()).getStatus().toString());
            agreementModel.setSummary(((MutableIssue) entry.getValue()).getSummary());
            model.addAgreement(agreementModel);
        }
        List<AgreementModel> agreementList= new ArrayList<>();
        List<AgreementModel> agreementListSupplier= new ArrayList<>();
        for (AgreementModel agreement : model.getAgreementsList()) {
            if (agreement.getType().equals("Поставщик"))
                agreementListSupplier.add(agreement);
            else
                agreementList.add(agreement);
        }


        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("issue", model.getKey());
        modelMap.put("pNumber", model.getProcedureNumber());
        modelMap.put("KP", model.getBuyAmount());
        modelMap.put("procurement", model.getSaleAmount());
        modelMap.put("margin", model.getMargin());
        modelMap.put("marginPercent", model.getMarginPercent());
        modelMap.put("agreementList", agreementList);
        modelMap.put("agreementListSupplier", agreementListSupplier);
//        modelMap.put("cr", issueWorker.getCfValue("Командировочные расходы", issue));
//        modelMap.put("fr", issueWorker.getCfValue("Финансовые расходы", issue));
//        modelMap.put("lr", issueWorker.getCfValue("Логистические расходы", issue));
//        modelMap.put("dr", issueWorker.getCfValue("Доп расходы", issue));
//        modelMap.put("amount", issueWorker.getCfValue("Сумма договора в руб с НДС", issue));
        return modelMap;
    }
}
