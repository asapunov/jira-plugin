package com.enviogroup.plugins.status.screen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatusModel {
    private String status;

    public StatusModel(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
