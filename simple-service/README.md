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




 More examples:
---------------

[Logging HTTP requests and responses using filters and name binding annotations](https://stackoverflow.com/a/33667568/1426227)

[Token-based authentication using filters and name binding annotations](https://stackoverflow.com/a/26778123/1426227)

[Using interceptors and name binding annotations to add a property to a JSON with Jackson](https://stackoverflow.com/a/36546268/1426227)

[Filters with name binding for security purposes](https://stackoverflow.com/a/33649605/1426227)

