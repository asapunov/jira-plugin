package com.enviogroup.plugins.status.screen;

public class ShipmentModel extends IssueModel {
    private OrganisationModel organisation;

    public OrganisationModel getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationModel organisation) {
        this.organisation = organisation;
    }
}
