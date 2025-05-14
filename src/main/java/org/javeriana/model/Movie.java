package org.javeriana.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Movie {

    private final UUID id;
    private String title;
    private String description;
    private String genre;
    private String director;
    private int durationInMinutes; // in minutes
    private String releaseDate;
    private String language;

    public Movie(
                 String title,
                 String description,
                 String genre,
                 String director,
                 int durationInMinutes,
                 String releaseDate,
                 String language) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.director = director;
        this.durationInMinutes = durationInMinutes;
        this.releaseDate = releaseDate;
        this.language = language;
    }

    public Movie(UUID id,
                 String title,
                 String description,
                 String genre,
                 String director,
                 int durationInMinutes,
                 String releaseDate,
                 String language) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.director = director;
        this.durationInMinutes = durationInMinutes;
        this.releaseDate = releaseDate;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }


    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public UUID getId() {
        return id;
    }
}
