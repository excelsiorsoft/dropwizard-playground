package com.excelsiorsoft.examples.resources;

import com.excelsiorsoft.examples.compress.Compress;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


@Path("helloworld")
public class HelloWorldResource {

    private final static Logger logger = LoggerFactory.getLogger(HelloWorldResource.class);

    @GET
    @Produces("text/plain")
    public String getHello() {
        return "Hello World!";
    }

    @GET
    @Path("too-much-data")
    @Compress
    public String getVeryLongString() {

        logger.debug("In HelloWorldResource#getVeryLongString()...");

        String str = "very long string to be compressed during request processing";
        return str;
    }

    @PUT
    @Path("redirect")
    public Response getRedirect() {
        return Response.notModified().build();
    }

    @Data
    public static class CreateRoleRequest{
        private String fieldA;
        private String fieldB;
    }

    @Data
    public static class CreateRoleResponse{
        private String fieldA;
        private String fieldB;
    }
}
