package com.excelsiorsoft.examples.dropwizard.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class HelloSayingFactory {

    @NotEmpty
    private String saying;

    @JsonProperty
    public String getSaying() {
        return saying;
    }
    @JsonProperty
    public void setSaying(String saying) {
        this.saying = saying;
    }
}
