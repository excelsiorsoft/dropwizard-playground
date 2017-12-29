package com.excelsiorsoft.examples.api;

import lombok.Data;

import java.util.Date;

@Data
public class Event {
    private long id;
    private String name;
    private String description;
    private String location;
    private Date date;

}