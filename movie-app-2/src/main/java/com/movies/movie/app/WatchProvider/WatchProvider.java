package com.movies.movie.app.WatchProvider;

import jakarta.persistence.Entity;


public class WatchProvider {
    private String providerName;
    private int providerId;
    private String logoPath;
    private String watchLink;
    private String type;

    // Constructors, getters, and setters


    public WatchProvider() {
    }

    // Constructor with all fields
    public WatchProvider(String providerName, int providerId, String logoPath, String watchLink) {
        this.providerName = providerName;
        this.providerId = providerId;
        this.logoPath = logoPath;
        this.watchLink = watchLink;
    }

    public WatchProvider(String providerName, int providerId, String logoPath, String watchLink, String type) {
        this.providerName = providerName;
        this.providerId = providerId;
        this.logoPath = logoPath;
        this.watchLink = watchLink;
        this.type = type;
    }

    // Getters and setters
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getWatchLink() {
        return watchLink;
    }

    public void setWatchLink(String watchLink) {
        this.watchLink = watchLink;
    }
}
