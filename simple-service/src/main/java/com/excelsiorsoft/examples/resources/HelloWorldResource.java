package com.excelsiorsoft.examples.resources;

import com.excelsiorsoft.examples.compress.Compress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


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
}
