package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.enviogroup.plugins.documentation.IssueWorker;

import java.util.HashMap;
import java.util.Map;

public class ModelMapper {
    private final Map entityConverterMap;
    private final IssueWorker issueWorker;
    private final Issue issue;

    public ModelMapper(JiraHelper jiraHelper, EntityConverter entityConverter) {
        this.entityConverterMap = entityConverter.getEntityConverterMap(jiraHelper);
        this.issueWorker = (IssueWorker) entityConverterMap.get("issueWorker");
        this.issue = (Issue) entityConverterMap.get("issue");
    }

    public Map<String, Object> getModelMap() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("issue", issue);
        modelMap.put("pNumber", issueWorker.getCfValue("№ Процедуры", issue));
        modelMap.put("NMC", issueWorker.getCfValue("Сумма НМЦ в руб", issue));
        modelMap.put("KP", issueWorker.getCfValue("KP", issue));
        modelMap.put("procurement", issueWorker.getCfValue("Сумма закупки в руб", issue));
        modelMap.put("margin", issueWorker.getCfValue("Маржа в руб", issue));
        modelMap.put("marginPercent", issueWorker.getCfValue("Маржа в %", issue));
        modelMap.put("cr", issueWorker.getCfValue("Командировочные расходы", issue));
        modelMap.put("fr", issueWorker.getCfValue("Финансовые расходы", issue));
        modelMap.put("lr", issueWorker.getCfValue("Логистические расходы", issue));
        modelMap.put("dr", issueWorker.getCfValue("Доп расходы", issue));
        modelMap.put("amount", issueWorker.getCfValue("Сумма договора в руб с НДС", issue));
        return modelMap;
    }
}
