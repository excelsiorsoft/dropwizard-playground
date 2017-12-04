package com.excelsiorsoft.examples.dropwizard.audit;

import org.joda.time.DateTime;

public class AuditInfo {

    private String method;
    private int responseCode;
    private DateTime date;
    private String entity;
    private String path;
    private String remoteAddress;
    private String username;

    public AuditInfo(String method, int responseCode, DateTime date, String entity, String path, String remoteAddress,
                     String username) {
        this.method = method;
        this.responseCode = responseCode;
        this.date = date;
        this.entity = entity;
        this.path = path;
        this.remoteAddress = remoteAddress;
        this.username = username;
    }

    public String getMethod() {
        return method;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public DateTime getDate() {
        return date;
    }

    public String getEntity() {
        return entity;
    }

    public String getPath() {
        return path;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUsername() {
        return username;
    }
}
