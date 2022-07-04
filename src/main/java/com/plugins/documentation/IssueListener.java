package com.plugins.documentation;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.issue.IssueViewEvent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;


/** Прослшиватель событий
 * предполгалаость, что на основании него будет обновляться документация
 * каждый раз при просмтре задач
 * Но пролушиватель событий при просмтре недоступн в весии 8.14*/
@Component
public class  IssueListener implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(IssueListener.class);
    private static final IssueManager issueManager = ComponentAccessor.getIssueManager();

    @JiraImport
    private final EventPublisher eventPublisher;

    @Autowired
    public IssueListener(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("I was");
        this.eventPublisher.register(this);
    }

    @Override
    public void destroy() throws Exception {
        this.eventPublisher.unregister(this);
    }

    @EventListener
    public void atIssueEvent(IssueEvent issueEvent) throws IOException {
        Issue issue = issueEvent.getIssue();
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject((long) 10000);
        log.warn(issue.getKey());
        log.warn(customField != null ? customField.getValueFromIssue(issue) : null);
    }
}