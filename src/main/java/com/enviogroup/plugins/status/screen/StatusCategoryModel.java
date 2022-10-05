package com.enviogroup.plugins.status.screen;

import com.atlassian.jira.issue.status.category.StatusCategory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatusCategoryModel {
    private Long id;
    private String name;
    private String key;
    private String colorName;

    public StatusCategoryModel(StatusCategory statusCategory) {
        this.key = statusCategory.getKey();
        this.name = statusCategory.getName();
        this.colorName = statusCategory.getColorName();
        this.id = statusCategory.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
