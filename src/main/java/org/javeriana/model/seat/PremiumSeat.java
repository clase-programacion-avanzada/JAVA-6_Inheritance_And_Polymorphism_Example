package org.javeriana.model.seat;

public class PremiumSeat extends Seat {


    public PremiumSeat(String row, int number, String status) {
        super(row, number, status);

    }

    public PremiumSeat(String row, int number) {
        super(row, number, AVAILABLE);
    }

    @Override
    public String getCategory() {
        return "Premium";
    }

    @Override
    public long getPrice() {
        return 10000;
    }

}
