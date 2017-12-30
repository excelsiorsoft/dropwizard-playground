# SimpleService

Takeaways:
- rootPath
- no need for annotating model

How to start the SimpleService application
------------------------------------------

$ curl -X GET http://localhost:8080/api/events

[{"id":10,"name":"Birthday","description":"Please do not be on time!","location":"345B Baker Street","date":"2017-12-28T23:31-0500"}]

Health Check
---

$ curl http://localhost:8081/healthcheck

{"deadlocks":{"healthy":true}}


Filters & Interceptors
----------------------
*Name binding* is a concept that allows to say to a JAX-RS runtime that a specific filter or interceptor will be executed only for a specific resource method.

When a filter or an interceptor is limited only to a specific resource method we say that it is *name-bound*.

Filters and interceptors that do not have such a limitation are called *global*.

Name-binding needs to be:

    - Defined

    - Bound to both interceptor/filter and relevant endpoint

    - Registered with JAX-RS (Jersey)


- Name-bound interceptor example:

  - see package: *com.excelsiorsoft.examples.compress*

  Interceptor must be registered with Jersey in the an App:

        environment.jersey().register(GZIPWriterInterceptor.class);

  Without the registration, it's not recognized and the response
  is plain text:

      $ curl -X GET http://localhost:8080/api/helloworld/too-much-data
      very long string to be compressed during request processing

  Once properly registered as shown above, the response is no longer text but binary:

      $ curl -X GET http://localhost:8080/api/helloworld/too-much-data
      Warning: Binary output can mess up your terminal. Use "--output -" to tell
      Warning: curl to output it to your terminal anyway, or consider "--output
      Warning: <FILE>" to save to a file.

The name-bound interceptor (designated by a @Compressed annotation in the example) is executed only if any resource method with this annotation is executed.
It's not executed for any other endpoints which are not decorated with such annotation:

HelloWorldResource#getHello() is not annotated - hence interceptor is bypassed:

    $ curl -X GET http://localhost:8080/api/helloworld/
    Hello World!

HelloWorldResource#getVeryLongString() is decorated - hence interceptor is executed:

    $ curl -X GET http://localhost:8080/api/helloworld/too-much-data
    Warning: Binary output can mess up your terminal. Use "--output -" to tell
    Warning: curl to output it to your terminal anyway, or consider "--output
    Warning: <FILE>" to save to a file.

If the name binding is applied on a resource class (HelloWorldResource would be annotated with @Compress) then all methods of such resource will be compressed.

Global filters are executed always, even for resource methods which have any name binding annotations.

See
   - [@NameBinding annotation documentation](https://docs.oracle.com/javaee/7/api/javax/ws/rs/NameBinding.html) and
   - [Jersey documentation about filters and interceptors](https://jersey.github.io/documentation/latest/user-guide.html#filters-and-interceptors)

 for details.

Logs
---

  Log levels are configurable (in **config.yml**):

      logging:
        level: INFO
        loggers:
          com.excelsiorsoft.examples: DEBUG

  and will result in the following output in the console:

          INFO  [2017-12-29 16:25:48,916] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@5d1b1c2a{/,null,AVAILABLE}
          INFO  [2017-12-29 16:25:49,056] org.eclipse.jetty.server.AbstractConnector: Started application@24e83d19{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
          INFO  [2017-12-29 16:25:49,057] org.eclipse.jetty.server.AbstractConnector: Started admin@5c080ef3{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
          INFO  [2017-12-29 16:25:49,057] org.eclipse.jetty.server.Server: Started @3465ms
          DEBUG [2017-12-29 16:25:54,801] com.excelsiorsoft.examples.resources.HelloWorldResource: In HelloWorldResource#getVeryLongString()...
          DEBUG [2017-12-29 16:25:54,808] com.excelsiorsoft.examples.compress.GZIPWriterInterceptor: about to compress in the interceptor
          INFO  [2017-12-29 16:25:54,808] com.excelsiorsoft.examples.compress.GZIPWriterInterceptor: done with compressing, proceeding...
          0:0:0:0:0:0:0:1 - - [29/Dec/2017:16:25:54 +0000] "GET /api/helloworld/too-much-data HTTP/1.1" 200 72 "-" "curl/7.55.0" 54

while this:

    logging:
      level: INFO
      appenders:
        - type: console
          threshold: ALL
          timeZone: America/New_York
          target: stdout
      loggers:
          com.excelsiorsoft.examples: DEBUG

 will give us proper local time as well as selective logging level:

     INFO  [2017-12-29 13:26:50,713] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@3a36cd5{/,null,AVAILABLE}
     INFO  [2017-12-29 13:26:50,779] org.eclipse.jetty.server.AbstractConnector: Started application@65a48602{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
     INFO  [2017-12-29 13:26:50,779] org.eclipse.jetty.server.AbstractConnector: Started admin@75483843{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
     INFO  [2017-12-29 13:26:50,780] org.eclipse.jetty.server.Server: Started @3699ms
     DEBUG [2017-12-29 13:26:58,399] com.excelsiorsoft.examples.resources.HelloWorldResource: In HelloWorldResource#getVeryLongString()...
     DEBUG [2017-12-29 13:26:58,405] com.excelsiorsoft.examples.compress.GZIPWriterInterceptor: about to compress in the interceptor!
     INFO  [2017-12-29 13:26:58,405] com.excelsiorsoft.examples.compress.GZIPWriterInterceptor: done with compressing, proceeding...
     0:0:0:0:0:0:0:1 - - [29/Dec/2017:18:26:58 +0000] "GET /api/helloworld/too-much-data HTTP/1.1" 200 72 "-" "curl/7.55.0" 50


Redirects:
----------

When client invocation is coded to return javax.ws.rs.core.Response, everything is hunky-dory:

    $ curl -v -X GET http://localhost:8080/api/client/healthy-redirect
    Note: Unnecessary use of -X or --request, GET is already inferred.
    * timeout on name lookup is not supported
    *   Trying ::1...
    * TCP_NODELAY set
    * Connected to localhost (::1) port 8080 (#0)
    > GET /api/client/healthy-redirect HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.55.0
    > Accept: */*
    >
    < HTTP/1.1 304 Not Modified
    < Date: Sat, 30 Dec 2017 03:59:34 GMT
    < Content-Type: application/json
    <
    * Connection #0 to host localhost left intact

---

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

---

       127.0.0.1 - - [30/Dec/2017:04:00:33 +0000] "PUT /api/helloworld/redirect HTTP/1.1" 304 0 "-" "Jersey/2.25.1 (HttpUrlConnection 1.8.0_144)" 2
       DEBUG [2017-12-29 23:00:33,599] com.excelsiorsoft.examples.resources.ClientInvocationResource: response length: -1 & status 304
       DEBUG [2017-12-29 23:00:33,599] com.excelsiorsoft.examples.resources.ClientInvocationResource: Here's my response: InboundJaxrsResponse{context=ClientResponse{method=PUT, uri=http://localhost:8080/api/helloworld/redirect, status=304, reason=Not Modified}}
       0:0:0:0:0:0:0:1 - - [30/Dec/2017:04:00:33 +0000] "GET /api/client/healthy-redirect HTTP/1.1" 304 0 "-" "curl/7.55.0" 33


However, when it's coded to return our own type (even if subtype of Response), not all is peachy.
Jersey treats 304 redirect status code as error:

    $ curl -v -X GET http://localhost:8080/api/client/exceptional-redirect
    Note: Unnecessary use of -X or --request, GET is already inferred.
    * timeout on name lookup is not supported
    *   Trying ::1...
    * TCP_NODELAY set
    * Connected to localhost (::1) port 8080 (#0)
    > GET /api/client/exceptional-redirect HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.55.0
    > Accept: */*
    >
    < HTTP/1.1 204 No Content
    < Date: Sat, 30 Dec 2017 04:05:52 GMT
    <
    * Connection #0 to host localhost left intact

---
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
---
        ERROR [2017-12-29 23:05:52,531] com.excelsiorsoft.examples.resources.ClientInvocationResource: {}
        ! javax.ws.rs.RedirectionException: HTTP 304 Not Modified
        ! at org.glassfish.jersey.client.JerseyInvocation.createExceptionForFamily(JerseyInvocation.java:1053)
        ! at org.glassfish.jersey.client.JerseyInvocation.convertToException(JerseyInvocation.java:1039)
        ! at org.glassfish.jersey.client.JerseyInvocation.translate(JerseyInvocation.java:819)
        ! at org.glassfish.jersey.client.JerseyInvocation.access$700(JerseyInvocation.java:92)
        ! at org.glassfish.jersey.client.JerseyInvocation$2.call(JerseyInvocation.java:701)
        ! at org.glassfish.jersey.internal.Errors.process(Errors.java:315)
        ! at org.glassfish.jersey.internal.Errors.process(Errors.java:297)
        ! at org.glassfish.jersey.internal.Errors.process(Errors.java:228)
        ! at org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:444)
        ! at org.glassfish.jersey.client.JerseyInvocation.invoke(JerseyInvocation.java:697)
        ! at org.glassfish.jersey.client.JerseyInvocation$Builder.method(JerseyInvocation.java:448)
        ! at org.glassfish.jersey.client.JerseyInvocation$Builder.put(JerseyInvocation.java:332)
        ! at com.excelsiorsoft.examples.resources.ClientInvocationResource.getExRedirect(ClientInvocationResource.java:53)
        ! at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        ! at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        ! at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        ! at java.lang.reflect.Method.invoke(Method.java:498)
        ! at org.glassfish.jersey.server.model.internal.ResourceMethodInvocationHandlerFactory$1.invoke(ResourceMethodInvocationHandlerFactory.java:81)
        ! at org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher$1.run(AbstractJavaResourceMethodDispatcher.java:144)
        ! at org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher.invoke(AbstractJavaResourceMethodDispatcher.java:161)
        ! at org.glassfish.jersey.server.model.internal.JavaResourceMethodDispatcherProvider$ResponseOutInvoker.doDispatch(JavaResourceMethodDispatcherProvider.java:160)
        ! at org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher.dispatch(AbstractJavaResourceMethodDispatcher.java:99)
        ! at org.glassfish.jersey.server.model.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:389)
        ! at org.glassfish.jersey.server.model.ResourceMethodInvoker.apply(ResourceMethodInvoker.java:347)
        ! at org.glassfish.jersey.server.model.ResourceMethodInvoker.apply(ResourceMethodInvoker.java:102)
        ! at org.glassfish.jersey.server.ServerRuntime$2.run(ServerRuntime.java:326)
        ! at org.glassfish.jersey.internal.Errors$1.call(Errors.java:271)
        ! at org.glassfish.jersey.internal.Errors$1.call(Errors.java:267)
        ! at org.glassfish.jersey.internal.Errors.process(Errors.java:315)
        ! at org.glassfish.jersey.internal.Errors.process(Errors.java:297)
        ! at org.glassfish.jersey.internal.Errors.process(Errors.java:267)
        ! at org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:317)
        ! at org.glassfish.jersey.server.ServerRuntime.process(ServerRuntime.java:305)
        ! at org.glassfish.jersey.server.ApplicationHandler.handle(ApplicationHandler.java:1154)
        ! at org.glassfish.jersey.servlet.WebComponent.serviceImpl(WebComponent.java:473)
        ! at org.glassfish.jersey.servlet.WebComponent.service(WebComponent.java:427)
        ! at org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:388)
        ! at org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:341)
        ! at org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:228)
        ! at io.dropwizard.jetty.NonblockingServletHolder.handle(NonblockingServletHolder.java:49)
        ! at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1650)
        ! at io.dropwizard.servlets.ThreadNameFilter.doFilter(ThreadNameFilter.java:34)
        ! at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1637)
        ! at io.dropwizard.jersey.filter.AllowedMethodsFilter.handle(AllowedMethodsFilter.java:45)
        ! at io.dropwizard.jersey.filter.AllowedMethodsFilter.doFilter(AllowedMethodsFilter.java:39)
        ! at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1637)
        ! at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:533)
        ! at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:188)
        ! at org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1253)
        ! at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:168)
        ! at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:473)
        ! at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:166)
        ! at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1155)
        ! at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
        ! at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:132)
        ! at com.codahale.metrics.jetty9.InstrumentedHandler.handle(InstrumentedHandler.java:241)
        ! at io.dropwizard.jetty.RoutingHandler.handle(RoutingHandler.java:52)
        ! at org.eclipse.jetty.server.handler.gzip.GzipHandler.handle(GzipHandler.java:527)
        ! at io.dropwizard.jetty.BiDiGzipHandler.handle(BiDiGzipHandler.java:68)
        ! at org.eclipse.jetty.server.handler.RequestLogHandler.handle(RequestLogHandler.java:56)
        ! at org.eclipse.jetty.server.handler.StatisticsHandler.handle(StatisticsHandler.java:169)
        ! at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:132)
        ! at org.eclipse.jetty.server.Server.handle(Server.java:561)
        ! at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:334)
        ! at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:251)
        ! at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:279)
        ! at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:104)
        ! at org.eclipse.jetty.io.ChannelEndPoint$2.run(ChannelEndPoint.java:124)
        ! at org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.doProduce(EatWhatYouKill.java:247)
        ! at org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.produce(EatWhatYouKill.java:140)
        ! at org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.run(EatWhatYouKill.java:131)
        ! at org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:243)
        ! at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:679)
        ! at org.eclipse.jetty.util.thread.QueuedThreadPool$2.run(QueuedThreadPool.java:597)
        ! at java.lang.Thread.run(Thread.java:748)
        0:0:0:0:0:0:0:1 - - [30/Dec/2017:04:05:52 +0000] "GET /api/client/exceptional-redirect HTTP/1.1" 204 0 "-" "curl/7.55.0" 42
        DEBUG [2017-12-29 23:05:52,532] com.excelsiorsoft.examples.resources.ClientInvocationResource: Here's my response: null



Adding ClientResponseFilter functionality:

    $ curl -v -X GET http://localhost:8080/api/client/redirect-with-body
    Note: Unnecessary use of -X or --request, GET is already inferred.
    * timeout on name lookup is not supported
    *   Trying ::1...
    * TCP_NODELAY set
    * Connected to localhost (::1) port 8080 (#0)
    > GET /api/client/redirect-with-body HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.55.0
    > Accept: */*
    >
    < HTTP/1.1 500 insufficient content written
    < Date: Sat, 30 Dec 2017 15:40:20 GMT
    < Cache-Control: must-revalidate,no-cache,no-store
    < Content-Type: text/html;charset=iso-8859-1
    < Content-Length: 299
    <
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>Error 500 insufficient content written</title>
    </head>
    <body><h2>HTTP ERROR 500</h2>
    <p>Problem accessing /api/client/redirect-with-body. Reason:
    <pre>    insufficient content written</pre></p>
    </body>
    </html>


    ---

    @GET
    @Path("redirect-with-body")
    public Response getRedirectWithBody() {

    class Collector{
        public String entity;
    }

    Collector collector = new Collector();

        HelloWorldResource.CreateRoleRequest item = new HelloWorldResource.CreateRoleRequest();
        Client client = ClientBuilder.newClient().register(
                (ClientResponseFilter) (requestContext, responseContext) -> {
                    int length = responseContext.getLength();
                    int status = responseContext.getStatus();

                    String response = extractResponse(responseContext);
                    collector.entity = response;


                    logger.debug("response: {}, response length: {} & status {} ", response, length, status);
                });
        WebTarget target = client.target("http://localhost:8080/api/helloworld/entity-redirect");

        Response response = target
                .request()
                .put(Entity.json(item));


        logger.debug("Here's my response: {}", response);
        logger.debug("Entity: {}", collector.entity.toString());
        return response;
    }
 ---
    DEBUG [2017-12-30 12:16:56,059] com.excelsiorsoft.examples.resources.ClientInvocationResource: response: {"fieldA":"201+", "fieldB":"description"}, response length: 41 & status 201
    127.0.0.1 - - [30/Dec/2017:17:16:56 +0000] "PUT /api/helloworld/entity-redirect HTTP/1.1" 201 41 "-" "Jersey/2.25.1 (HttpUrlConnection 1.8.0_144)" 16
    DEBUG [2017-12-30 12:16:56,059] com.excelsiorsoft.examples.resources.ClientInvocationResource: Here's my response: InboundJaxrsResponse{context=ClientResponse{method=PUT, uri=http://localhost:8080/api/helloworld/entity-redirect, status=201, reason=Created}}
    DEBUG [2017-12-30 12:16:56,062] com.excelsiorsoft.examples.resources.ClientInvocationResource: Entity: {"fieldA":"201+", "fieldB":"description"}
    0:0:0:0:0:0:0:1 - - [30/Dec/2017:17:16:56 +0000] "GET /api/client/redirect-with-body HTTP/1.1" 500 299 "-" "curl/7.55.0" 153







 More examples:
---------------

[Logging HTTP requests and responses using filters and name binding annotations](https://stackoverflow.com/a/33667568/1426227)

[Token-based authentication using filters and name binding annotations](https://stackoverflow.com/a/26778123/1426227)

[Using interceptors and name binding annotations to add a property to a JSON with Jackson](https://stackoverflow.com/a/36546268/1426227)

[Filters with name binding for security purposes](https://stackoverflow.com/a/33649605/1426227)

