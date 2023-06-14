package com.movies.movie.app.People;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

    private Long id;
    private String name;
    private String character;
    private String job;
    private String department;
    private String profilePath;

    public Person() {
        // Default constructor
    }

    public Person(Long id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("character")
    public String getCharacter() {
        return character;
    }

    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("profile_path")
    public String getProfilePath() {
        return profilePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
