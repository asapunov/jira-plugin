package com.enviogroup.plugins.status.screen.letters;

import com.enviogroup.plugins.status.screen.OrganisationModel;

public class OutputLetterModel extends BaseLetterModel {

    private OrganisationModel recipientOrg;
    private InputLetterModel childInputLetter;
    private InputLetterModel parentInputLetter;

    public OrganisationModel getRecipientOrg() {
        return recipientOrg;
    }

    public void setRecipientOrg(OrganisationModel recipientOrg) {
        this.recipientOrg = recipientOrg;
    }

    public InputLetterModel getChildInputLetter() {
        return childInputLetter;
    }

    public void setChildInputLetter(InputLetterModel childInputLetter) {
        this.childInputLetter = childInputLetter;
    }

    public InputLetterModel getParentInputLetter() {
        return parentInputLetter;
    }

    public void setParentInputLetter(InputLetterModel parentInputLetter) {
        this.parentInputLetter = parentInputLetter;
    }
}
