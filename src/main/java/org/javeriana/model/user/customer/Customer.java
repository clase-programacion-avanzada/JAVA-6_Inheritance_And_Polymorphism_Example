package org.javeriana.model.user.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.javeriana.model.Ticket;
import org.javeriana.model.user.User;

public abstract class Customer extends User {


    private final List<Ticket> tickets;

    public Customer(UUID id, String name, String email, String password) {
        super(id, name, email, password);
        this.tickets = new ArrayList<>();
    }

    public Customer(String name, String email, String password) {
        this(UUID.randomUUID(), name, email, password);
    }

    // Abstract method to get customer discount
    public abstract double getDiscount();

    // Method to add a ticket to customer's list
    public void addTicket(Ticket ticket) {
        
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        if (tickets.contains(ticket)) {
            throw new IllegalArgumentException("Ticket already exists in the customer's list");
        }

        tickets.add(ticket);
    }

    // Method to get all tickets
    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }

    @Override
    public String toString() {
        return"id " + id +
            "| name '" + name + '\'' +
            "| email '" + email + '\'' +
            "| password '" + password;
    }
}
