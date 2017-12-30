package com.excelsiorsoft.examples.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

//

@Path("client")
@Produces(MediaType.APPLICATION_JSON)
public class ClientInvocationResource {

    private final static Logger logger = LoggerFactory.getLogger(ClientInvocationResource.class);

    private static final ObjectMapper jacksonMapper = new ObjectMapper();

    @GET
    @Path("healthy-redirect")
    public Response getHealthyRedirect() {

        HelloWorldResource.CreateRoleRequest item = new HelloWorldResource.CreateRoleRequest();
        Client client = ClientBuilder.newClient().register(
                (ClientResponseFilter) (requestContext, responseContext) -> {
                    int length = responseContext.getLength();
                    int status = responseContext.getStatus();
                    logger.debug("response length: {} & status {}", length, status);
                });
        WebTarget target = client.target("http://localhost:8080/api/helloworld/redirect");

        Response response = target
                .request()
                .put(Entity.json(item));

        logger.debug("Here's my response: {}", response);
        return response;
    }

    @GET
    @Path("exceptional-redirect")
    public HelloWorldResource.CreateRoleResponse getExRedirect() {

        HelloWorldResource.CreateRoleRequest item = new HelloWorldResource.CreateRoleRequest();
        Client client = ClientBuilder.newClient()/*.register(new RedirectReaderInterceptor())*/;
        WebTarget target = client.target("http://localhost:8080/api/helloworld/redirect");

        HelloWorldResource.CreateRoleResponse response = null;
        try {
            response = target
                    .request()
                    .put(Entity.json(item), HelloWorldResource.CreateRoleResponse.class);
        }catch(Exception e){
            logger.error("{}", e);
        }
        logger.debug("Here's my response: {}", response);
        return response;
    }




    @GET
    @Path("redirect-with-body")
    public Response getRedirectWithBody() {



        HelloWorldResource.CreateRoleRequest item = new HelloWorldResource.CreateRoleRequest();
        Client client = ClientBuilder.newClient().register(
                (ClientResponseFilter) (requestContext, responseContext) -> {
                    int length = responseContext.getLength();
                    int status = responseContext.getStatus();


                    String response = extractResponse(responseContext);

                    logger.debug("response: {}, response length: {} & status {} ", response, length, status);
                });
        WebTarget target = client.target("http://localhost:8080/api/helloworld/entity-redirect");

        Response response = target
                .request()
                .put(Entity.json(item));


        logger.debug("Here's my response: {}", response);
        return response;
    }

    private String extractResponse(ClientResponseContext responseContext) throws IOException {
        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return copyStream(responseContext.getEntityStream());
            }
        };

        return byteSource.asCharSource(Charsets.UTF_8).read();
    }

    private static InputStream copyStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        InputStream copy = new ByteArrayInputStream(baos.toByteArray());

        return copy;
    }

    /*private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }*/

}
