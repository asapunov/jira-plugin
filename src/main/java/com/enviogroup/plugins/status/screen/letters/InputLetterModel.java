package com.enviogroup.plugins.status.screen.letters;

import com.enviogroup.plugins.status.screen.OrganisationModel;

public class InputLetterModel extends OutputLetterModel {

    private OrganisationModel senderOrg;
    private OutputLetterModel childOutputLetter;
    private OutputLetterModel parentOutputLetter;

    public OrganisationModel getSenderOrg() {
        return senderOrg;
    }

    public void setSenderOrg(OrganisationModel senderOrg) {
        this.senderOrg = senderOrg;
    }

    public OutputLetterModel getChildOutputLetter() {
        return childOutputLetter;
    }

    public void setChildOutputLetter(OutputLetterModel childOutputLetter) {
        this.childOutputLetter = childOutputLetter;
    }

    public OutputLetterModel getParentOutputLetter() {
        return parentOutputLetter;
    }

    public void setParentOutputLetter(OutputLetterModel parentOutputLetter) {
        this.parentOutputLetter = parentOutputLetter;
    }
}
