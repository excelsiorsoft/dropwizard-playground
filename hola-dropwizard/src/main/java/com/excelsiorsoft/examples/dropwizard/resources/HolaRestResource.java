package com.excelsiorsoft.examples.dropwizard.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/api")
public class HolaRestResource {


    private String saying;

    public HolaRestResource(final String saying) {
        this.saying = saying;
    }

    @Path("/hola")
    @GET
    public String hola() throws UnknownHostException {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost()
                    .getHostAddress();
        } catch (UnknownHostException e) {
            hostname = "unknown";
        }
        return /*"Hola Dropwizard de "*/ saying + hostname;
    }

}
