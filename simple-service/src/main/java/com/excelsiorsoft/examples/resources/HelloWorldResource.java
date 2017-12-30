package com.excelsiorsoft.examples.resources;

import com.excelsiorsoft.examples.compress.Compress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


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

    @PUT
    @Path("entity-redirect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntityRedirect() {

        /*HealthyCreateRoleResponse respEntity = new HealthyCreateRoleResponse();
        respEntity.setFieldA("201+");
        respEntity.setFieldB("description");*/
        return Response.status(201).entity(
                /*respEntity*/
               "{\"fieldA\":\"201+\", \"fieldB\":\"description\"}"
        ).build();

    }

    @Data
    public static class CreateRoleRequest{
        private String fieldA;
        private String fieldB;
    }

    public static /*abstract*/ class MixIn extends Response{

        @Override
        @JsonIgnore
        public int getStatus() {
            return 0;
        }

        @Override
        @JsonIgnore
        public StatusType getStatusInfo() {
            return null;
        }

        @Override
        @JsonIgnore
        public Object getEntity() {
            return null;
        }

        @Override
        @JsonIgnore
        public <T> T readEntity(Class<T> entityType) {
            return null;
        }

        @Override
        @JsonIgnore
        public <T> T readEntity(GenericType<T> entityType) {
            return null;
        }

        @Override
        @JsonIgnore
        public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
            return null;
        }

        @Override
        @JsonIgnore
        public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
            return null;
        }

        @Override
        @JsonIgnore
        public boolean hasEntity() {
            return false;
        }

        @Override
        @JsonIgnore
        public boolean bufferEntity() {
            return false;
        }

        @Override
        @JsonIgnore
        public void close() {

        }

        @Override
        @JsonIgnore
        public MediaType getMediaType() {
            return null;
        }

        @Override
        @JsonIgnore
        public Locale getLanguage() {
            return null;
        }

        @Override
        @JsonIgnore
        public int getLength() {
            return 0;
        }

        @Override
        @JsonIgnore
        public Set<String> getAllowedMethods() {
            return null;
        }

        @Override
        @JsonIgnore
        public Map<String, NewCookie> getCookies() {
            return null;
        }

        @Override
        @JsonIgnore
        public EntityTag getEntityTag() {
            return null;
        }

        @Override
        @JsonIgnore
        public Date getDate() {
            return null;
        }

        @Override
        @JsonIgnore
        public Date getLastModified() {
            return null;
        }

        @Override
        @JsonIgnore
        public URI getLocation() {
            return null;
        }

        @Override
        @JsonIgnore
        public Set<Link> getLinks() {
            return null;
        }

        @Override
        @JsonIgnore
        public boolean hasLink(String relation) {
            return false;
        }

        @Override
        @JsonIgnore
        public Link getLink(String relation) {
            return null;
        }

        @Override
        @JsonIgnore
        public Link.Builder getLinkBuilder(String relation) {
            return null;
        }

        @Override
        @JsonIgnore
        public MultivaluedMap<String, Object> getMetadata() {
            return null;
        }

        @Override
        @JsonIgnore
        public MultivaluedMap<String, String> getStringHeaders() {
            return null;
        }

        @Override
        @JsonIgnore
        public String getHeaderString(String name) {
            return null;
        }
    }

    @Data
    public static class CreateRoleResponse extends MixIn {

        private String fieldA;

        private String fieldB;


    }

    /*@Data
    public static class HealthyCreateRoleResponse {

        //@JsonProperty
        private String fieldA;

       // @JsonProperty
        private String fieldB;

        public HealthyCreateRoleResponse(){}

    }*/

    public Response createHealthyCreateRoleResponse() {
        String result = "{\"fieldA\":\"201\", \"fieldB\":\"description\"}";
        return Response.status(201).entity(result).build();

    }


}
