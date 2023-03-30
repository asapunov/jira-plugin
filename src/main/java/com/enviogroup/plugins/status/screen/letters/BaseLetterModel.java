package com.enviogroup.plugins.status.screen.letters;

import com.enviogroup.plugins.status.screen.IssueModel;

public class BaseLetterModel extends IssueModel {
    private String letterType;

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }
}
