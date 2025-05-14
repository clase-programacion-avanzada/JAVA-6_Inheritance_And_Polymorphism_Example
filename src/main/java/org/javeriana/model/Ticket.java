package org.javeriana.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.javeriana.model.user.customer.Customer;
import org.javeriana.model.seat.Seat;

public class Ticket {

    private UUID id;
    private Set<Seat> seats;
    private Show show;
    private LocalDateTime dateOfPurchase;
    private Customer customer;

    public Ticket(UUID id,
                  Set<Seat> seats,
                  Show show,
                  Customer customer) {
        this.id = id;
        this.seats = seats;
        this.show = show;
        this.customer = customer;
        this.dateOfPurchase = LocalDateTime.now();
    }

    public Ticket(Set<Seat> seats,
                  Show show,
                  Customer customer) {
        this(UUID.randomUUID(), seats, show, customer);
    }

    public long getPrice() {

        long price = 0;

        for (Seat s : seats) {
            price += s.getPrice();
        }

        return price;
    }
    
    public long getFinalPrice() {
        long basePrice = getPrice();
        double discount = customer.getDiscount();
        return Math.round(basePrice * (1 - discount));
    }

    public List<String> ticketDetails() {

        String seatsDetails = seats.stream()
            .map(Seat::getSeatDetails)
            .reduce((a, b) -> a + ", " + b)
            .orElse("");

        return List.of(
            "Ticket ID: " + id,
            "Show Details: " + show.getShowDetails(),
            "Seats: " + seatsDetails,
            "Price without discount: " + getPrice(),
            "Discount: " + (customer.getDiscount() * 100) + "%",
            "Final price: " + getFinalPrice(),
            "Date of Purchase: " + dateOfPurchase
        );
    }
    
    public UUID getId() {
        return id;
    }

    public Set<Seat> getSeats() {
        return new HashSet<>(seats);
    }
}
