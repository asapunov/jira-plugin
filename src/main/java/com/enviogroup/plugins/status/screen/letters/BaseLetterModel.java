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

    private BaseLetterModel childLetter;

    private BaseLetterModel parentLetter;

    public BaseLetterModel getChildLetter() {
        return childLetter;
    }

    public void setChildLetter(BaseLetterModel childLetter) {
        this.childLetter = childLetter;
    }

    public BaseLetterModel getParentLetter() {
        return parentLetter;
    }

    public void setParentLetter(BaseLetterModel parentLetter) {
        this.parentLetter = parentLetter;
    }
}
