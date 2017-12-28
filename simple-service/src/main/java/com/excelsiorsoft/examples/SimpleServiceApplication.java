package com.excelsiorsoft.examples;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: implement application
    }

}
