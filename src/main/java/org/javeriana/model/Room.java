package org.javeriana.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.javeriana.model.seat.Seat;

public class Room {

    private final UUID id;
    private final Map<String, List<Seat>> seats;
    private final List<Show> shows;
    private int maxColumns;
    private String name;

    public Room(String name) {
        this.id = UUID.randomUUID();
        this.seats = new LinkedHashMap<>();
        this.shows = new ArrayList<>();
        this.name = name;
        this.maxColumns = 0;
    }

    public String getName() {
        return name;
    }

    public void createShow(LocalDateTime showTime,
                        Movie movie) {

        //Validates if there is a conflict with the show time
        // A conflict is when two shows are at the same time or
        // when a show is at the same time as the end of another
        for (Show show : shows) {

            LocalDateTime startTime = show.getShowTime();
            LocalDateTime endTime = Show.calculateEndTime(startTime, movie.getDurationInMinutes());

            if((showTime.isAfter(startTime) && showTime.isBefore(endTime))
            || showTime.isEqual(startTime)
            || showTime.isEqual(endTime)
            ) {
                throw new IllegalArgumentException("Show time conflict with another show");
            }
        }

        Show show = new Show(
            showTime,
            movie
        );

        this.shows.add(show);
    }

    public void addRowOfSeats(int size,
                              String row,
                              String seatType,
                              Set<Integer> skipNumbers
                        ) {

        List<Seat> seatList = Seat.createSeats(
            size,
            row,
            skipNumbers,
            seatType
        );

        if (this.maxColumns < size) {
            this.maxColumns = size;
        }

        this.seats.put(row, seatList);
    }

    public void addRowOfSeats(int size,
                              String row,
                              String seatType
    ) {
        Set<Integer> skipNumbers = Set.of();
        List<Seat> seatList = Seat.createSeats(
            size,
            row,
            skipNumbers,
            seatType
        );

        this.seats.put(row, seatList);
    }

    public boolean bookSeat(String row,
                            int number) {

        List<Seat> seatList = this.seats.getOrDefault(
            row,
            new ArrayList<>());

        for (Seat seat : seatList) {
            if (seat.getNumber() == number && seat.getStatus().equalsIgnoreCase(Seat.AVAILABLE)) {
                seat.setStatus(Seat.NOT_AVAILABLE);
                return true;
            }
        }

        return false;
    }

    public UUID getId() {
        return id;
    }

    public List<Show> getShows() {
        return shows;
    }

    public Seat getSeat(String row, int number) {
        List<Seat> seatList = seats.getOrDefault(row, new ArrayList<>());
        for (Seat seat : seatList) {
            if (seat.getNumber() == number) {
                return seat;
            }
        }
        return null;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (List<Seat> seatList : seats.values()) {
            for (Seat seat : seatList) {
                if (seat.getStatus().equals(Seat.AVAILABLE)) {
                    availableSeats.add(seat);
                }
            }
        }
        return availableSeats;
    }

    public Show getShowById(UUID showId) {
        for (Show show : shows) {
            if (show.getId().equals(showId)) {
                return show;
            }
        }
        return null;
    }

    public void validateSeatIdentifiers(Set<Seat> seats) {
        boolean areAllSeatsAvailable = true;

        for (Seat seat : seats) {

            if (!seat.getStatus().equals(Seat.AVAILABLE)) {
                areAllSeatsAvailable = false;
                break;
            }
        }

        if (!areAllSeatsAvailable) {
            throw new IllegalArgumentException("Some seats are not available");
        }
    }

    public Set<Seat> getSeatsFromIdentifiers(Set<String> seatIdentifiers) {
        Set<Seat> bookedSeats = new HashSet<>();
        for (String seatId : seatIdentifiers) {

            String[] parts = seatId.split("-");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid seat format: " + seatId);
            }

            String row = parts[0];
            int number = Integer.parseInt(parts[1]);

            Seat seat = getSeat(row, number);

            if (seat == null) {
                throw new IllegalArgumentException("Seat " + seatId + " does not exist");
            }

            bookedSeats.add(seat);

        }
        return bookedSeats;
    }

    public Set<Seat> bookSeats(Set<String> seatIdentifiers) {

        Set<Seat> seatsToBook = getSeatsFromIdentifiers(seatIdentifiers);

        validateSeatIdentifiers(seatsToBook);

        Set<Seat> bookedSeats = new HashSet<>();

        for (Seat seat : seatsToBook) {
            if (bookSeat(seat.getRow(), seat.getNumber())) {
                bookedSeats.add(seat);
            }
        }

        return bookedSeats;

    }

    public void releaseSeats(Set<Seat> bookedSeats) {
        for (Seat seat : bookedSeats) {
            seat.setStatus(Seat.AVAILABLE);
        }

    }
}
