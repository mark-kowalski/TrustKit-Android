package com.datatheorem.android.trustkit.config.model;

public class Domain {
    private Boolean includeSubdomains = null;
    private String hostName = null;

    public Boolean getIncludeSubdomains() {
        return includeSubdomains;
    }

    public void setIncludeSubdomains(Boolean includeSubdomains) {
        this.includeSubdomains = includeSubdomains;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}