package com.excelsiorsoft.examples.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("client")
@Produces(MediaType.APPLICATION_JSON)
public class ClientInvocationResource {

    private final static Logger logger = LoggerFactory.getLogger(ClientInvocationResource.class);

    @GET
    @Path("redirect")
    public Response getRedirect() {

        HelloWorldResource.CreateRoleRequest item = new HelloWorldResource.CreateRoleRequest();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/api/helloworld/redirect");

        Response response = target
                .request()
                .put(Entity.json(item));

        logger.debug("Here's my response: {}", response);
        return response;
    }


}
