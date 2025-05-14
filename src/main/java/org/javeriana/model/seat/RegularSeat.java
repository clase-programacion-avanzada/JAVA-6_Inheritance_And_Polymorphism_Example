package org.javeriana.model.seat;

public class RegularSeat extends Seat {

    public RegularSeat(String row, int number, String status) {
        super(row, number, status);
    }

    public RegularSeat(String row, int number) {
        super(row, number, AVAILABLE);
    }

    @Override
    public String getCategory() {
        return "Regular";
    }

    @Override
    public long getPrice() {
        return 5000;
    }
}
