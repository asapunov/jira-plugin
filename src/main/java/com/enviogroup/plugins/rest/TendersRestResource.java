package com.enviogroup.plugins.rest;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A resource of message.
 */
@Path("/tenders")
public class TendersRestResource {
    @POST
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMessage(@FormParam("keys") String keys) {
        if (keys != null) {
            return Response.ok(new TendersRestResourceModel(getMessageFromKey(keys))).build();
        } else {
            return Response.ok(new TendersRestResourceModel("Error")).build();
        }
    }

    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{key}")
    public Response getMessageFromPath(@PathParam("key") String key) {

        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        String test = key;
        return Response.ok(new TendersRestResourceModel(getMessageFromKey(test))).build();
    }

    private ArrayList<String> getMessageFromKey(String key) {
        return new ArrayList<String>(Arrays.asList(key.split(",")));
    }
}