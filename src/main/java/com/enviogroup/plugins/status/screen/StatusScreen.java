package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.velocity.NumberTool;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatusScreen extends AbstractJiraContextProvider {

    private static final NumberTool numberTool = new NumberTool(new Locale("ru", "RU"));
    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        ModelMapper modelMapper = new ModelMapper(jiraHelper, new EntityConverter());
        Map<String, Object> contextMap = new HashMap<>(modelMapper.getModelMap());
        contextMap.put("number", numberTool);
        contextMap.put("soyRenderer", ComponentAccessor.getComponent(SoyTemplateRendererProvider.class).getRenderer());
        return contextMap;
    }
}
