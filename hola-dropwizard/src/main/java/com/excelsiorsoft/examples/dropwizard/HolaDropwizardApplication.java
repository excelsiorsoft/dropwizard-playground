package com.excelsiorsoft.examples.dropwizard;

import com.excelsiorsoft.examples.dropwizard.resources.HolaRestResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HolaDropwizardApplication extends Application<HolaDropwizardConfiguration> {

        public static void main(final String[] args) throws Exception {
            new HolaDropwizardApplication().run(args);
        }

    @Override
    public String getName() {
        return "HolaDropwizard";
    }

    @Override
    public void initialize(final Bootstrap<HolaDropwizardConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public void run(final HolaDropwizardConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new HolaRestResource(configuration.getSayingFactory().getSaying()));
    }

}
