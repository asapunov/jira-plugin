package com.enviogroup.plugins.status.screen.iterfaces;

import com.atlassian.jira.issue.status.Status;
import com.enviogroup.plugins.status.screen.StatusModel;

public interface IssueModelImpl {
    String getKey();

    void setKey(String key);

    String getSummary();

    void setSummary(String summary);

    StatusModel getStatus();

    void setStatus(StatusModel status);

    void setStatus(Status status);
}
