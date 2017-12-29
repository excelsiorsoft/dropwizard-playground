package com.excelsiorsoft.examples;

import com.excelsiorsoft.examples.compress.GZIPWriterInterceptor;
import com.excelsiorsoft.examples.resources.EventResource;
import com.excelsiorsoft.examples.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SimpleServiceApplication extends Application<SimpleServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new com.excelsiorsoft.examples.SimpleServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "simpleService";
    }

    @Override
    public void initialize(final Bootstrap<SimpleServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final SimpleServiceConfiguration configuration,
                    final Environment environment) {

        DateFormat eventDateFormat = new SimpleDateFormat(configuration.getDateFormat());
        environment.getObjectMapper().setDateFormat(eventDateFormat);

        environment.jersey().register(new EventResource());
        environment.jersey().register(new HelloWorldResource());
        environment.jersey().register(GZIPWriterInterceptor.class);

    }

}
