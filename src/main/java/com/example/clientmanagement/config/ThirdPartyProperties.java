package com.example.clientmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.third-party")
public class ThirdPartyProperties {

    private String geoBaseUrl = "https://jsonplaceholder.typicode.com";

    private long userOffset = 1;

    public String getGeoBaseUrl() {
        return geoBaseUrl;
    }

    public void setGeoBaseUrl(String geoBaseUrl) {
        this.geoBaseUrl = geoBaseUrl;
    }

    public long getUserOffset() {
        return userOffset;
    }

    public void setUserOffset(long userOffset) {
        this.userOffset = userOffset;
    }
}
