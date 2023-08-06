package com.movies.movie.app.WatchProvider;


import java.util.List;

public class WatchProvidersContainer {

    private String Country;
    //link to TMDB with all the links to the movies
    private String link;
    private List<WatchProvider> flatrate;
    private List<WatchProvider> rent;
    private List<WatchProvider> buy;

    public WatchProvidersContainer() {
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<WatchProvider> getFlatrate() {
        return flatrate;
    }

    public void setFlatrate(List<WatchProvider> flatrate) {
        this.flatrate = flatrate;
    }

    public List<WatchProvider> getRent() {
        return rent;
    }

    public void setRent(List<WatchProvider> rent) {
        this.rent = rent;
    }

    public List<WatchProvider> getBuy() {
        return buy;
    }

    public void setBuy(List<WatchProvider> buy) {
        this.buy = buy;
    }
}
