package org.javeriana.model.seat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Seat {

    public static final String AVAILABLE = "AVAILABLE";
    public static final String NOT_AVAILABLE = "NOT_AVAILABLE";
    public static final String NOT_EXIST = "NOT_EXIST";

    private String row;
    private int number;

    /* A seat can have three states:
     * 1. Available //Not booked
     * 2. Not available //Booked
     * 3. Not exist
     *
     * The best way to represent this is by using an Enum. However, it's
     * out of the scope of the course. So, we will use a String.
     */
    private String status;


    public Seat(String row, int number, String status) {

        this.row = row;
        this.number = number;
        this.status = getStatus(status);
    }

    private static String getStatus(String status) {

        String statusUpper = status.toUpperCase();

        return switch(statusUpper) {
            case AVAILABLE -> AVAILABLE;
            case NOT_AVAILABLE -> NOT_AVAILABLE;
            default -> NOT_EXIST;
        };
    }

    public static List<Seat> createSeats(int size,
                                         String row,
                                        Set<Integer> skipNumbers,
                                        String category) {
        List<Seat> seats = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int number = i + 1;
            if (!skipNumbers.contains(number)) {
                if (category.equalsIgnoreCase("Regular")) {
                    seats.add(new RegularSeat(row, number));
                } else if (category.equals("Premium")) {
                    seats.add(new PremiumSeat(row, number));
                }
            } else {
                seats.add(new RegularSeat(row, number, NOT_EXIST));
            }
        }

        return seats;
    }

    public String getRow() {
        return row;
    }

    public void setStatus(String status) {
        this.status = getStatus(status);
    }

    public int getNumber() {
        return number;
    }

    public String getStatus() {
       return status;
    }

    public String getSeatDetails() {
        return row + number;
    }

    public String getRepresentation() {

        if (status.equals(NOT_EXIST)) {
            return "";
        }

        String seatContent = switch (status) {
            case AVAILABLE -> "O";
            case NOT_AVAILABLE -> "X";
            default -> "";
        };

        return this instanceof RegularSeat
            ? " [" + seatContent + "] "
            : "*[" + seatContent + "]*";
    }

    public abstract String getCategory();

    public abstract long getPrice();

    public final String getStatusString() {
        return status;
    }
}

