package org.javeriana.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Show {

    private static final int WRAP_UP_TIME_IN_MINUTES = 30; // in minutes
    private final UUID id;
    private final LocalDateTime showTime;
    private final Movie movie;

    public Show(
                LocalDateTime showTime,
                Movie movie) {
        this.id = UUID.randomUUID();
        this.showTime = showTime;
        this.movie = movie;
    }

    public static LocalDateTime calculateEndTime(LocalDateTime showTime,
                                                 int durationInMinutes) {

        return showTime.plusMinutes(durationInMinutes + WRAP_UP_TIME_IN_MINUTES);
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public UUID getId() {
        return id;
    }

    public String getShowDetails() {
        return "\n Show Time: " + showTime +
            "\n Movie: " + movie.getTitle();
    }


}
