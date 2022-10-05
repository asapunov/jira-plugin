package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.issue.status.category.StatusCategory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatusModel {
    private String id;
    private String name;
    private String description;
    private String iconUrl;
    private StatusCategoryModel statusCategory;

    public StatusModel(Status status) {
        this.id = status.getSimpleStatus().getId();
        this.name = status.getSimpleStatus().getName();
        this.description = status.getSimpleStatus().getDescription();
        this.iconUrl = status.getSimpleStatus().getIconUrl();
        this.statusCategory = new StatusCategoryModel(status.getStatusCategory());
    }


    public void setStatus(Status status) {
        this.id = status.getSimpleStatus().getId();
        this.name = status.getSimpleStatus().getName();
        this.description = status.getSimpleStatus().getDescription();
        this.iconUrl = status.getSimpleStatus().getIconUrl();
        this.statusCategory = new StatusCategoryModel(status.getStatusCategory());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public StatusCategoryModel getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(StatusCategoryModel statusCategory) {
        this.statusCategory = statusCategory;
    }
}
