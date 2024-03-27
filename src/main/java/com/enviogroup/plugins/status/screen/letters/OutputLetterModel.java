package com.enviogroup.plugins.status.screen.letters;

import com.enviogroup.plugins.status.screen.OrganisationModel;

public class OutputLetterModel extends BaseLetterModel {

    private OrganisationModel recipientOrg;
    public OrganisationModel getRecipientOrg() {
        return recipientOrg;
    }

    private static final String OUTPUT_LETTER_TYPE = "Исходящее письмо";

    @Override
    public String getLetterType() {
        return OUTPUT_LETTER_TYPE;
    }
}
