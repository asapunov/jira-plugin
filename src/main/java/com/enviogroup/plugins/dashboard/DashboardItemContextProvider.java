package com.enviogroup.plugins.dashboard;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.ContextProvider;
import com.google.common.collect.Maps;

import java.util.Map;

public class DashboardItemContextProvider implements ContextProvider {
    private final PluginAccessor pluginAccessor;

    public DashboardItemContextProvider(
            @ComponentImport PluginAccessor pluginAccessor) {
        this.pluginAccessor = pluginAccessor;
    }

    @Override
    public void init(final Map<String, String> params) throws PluginParseException {
    }

    @Override
    public Map<String, Object> getContextMap(final Map<String, Object> context) {
        final Map<String, Object> newContext = Maps.newHashMap(context);
        Plugin plugin = pluginAccessor.getEnabledPlugin("com.plugins.customfield.jira-custom-field");
        newContext.put("version", plugin.getPluginInformation().getVersion());
        newContext.put("pluginName", plugin.getName());
        return newContext;
    }
}