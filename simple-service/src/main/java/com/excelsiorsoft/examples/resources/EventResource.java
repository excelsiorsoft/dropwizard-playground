package com.excelsiorsoft.examples.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.excelsiorsoft.examples.api.Event;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
    @GET
    public List<Event> allEvents() {
        Event e = new Event();
        e.setDate(new Date());
        e.setName("Birthday");
        e.setId(10L);
        e.setDescription("Please do not be on time!");
        e.setLocation("345B Baker Street");
        return Collections.singletonList(e);
    }
}
