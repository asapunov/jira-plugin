package com.enviogroup.plugins.status.screen.letters;

import com.enviogroup.plugins.status.screen.OrganisationModel;

public class InputLetterModel extends OutputLetterModel {

    private OrganisationModel senderOrg;

    public OrganisationModel getSenderOrg() {
        return senderOrg;
    }

    public void setSenderOrg(OrganisationModel senderOrg) {
        this.senderOrg = senderOrg;
    }


}
