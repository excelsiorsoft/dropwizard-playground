package com.excelsiorsoft.examples;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class SimpleServiceConfiguration extends Configuration {

    @NotEmpty
    private String dateFormat;

    public String getDateFormat() {
        return dateFormat;
    }
}
