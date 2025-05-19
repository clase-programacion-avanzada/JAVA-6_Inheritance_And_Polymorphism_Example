package org.javeriana.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javeriana.model.user.customer.Customer;
import org.javeriana.model.seat.Seat;
import org.javeriana.payment.PaymentSystem;

public class Cinema {

    private final List<Room> rooms;
    private final List<Movie> movies;
    private final List<Customer> customers;
    private final List<Ticket> tickets;

    private final Map<String, PaymentSystem> paymentSystems;

    public Cinema(List<PaymentSystem> paymentSystems) {
        this.rooms = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.paymentSystems =paymentSystems.stream()
                .collect(
                    Collectors.toMap(
                        PaymentSystem::getPaymentName, 
                        Function.identity()));
    }

    public void createRoom(String name) {
        Room room = new Room(name);
        rooms.add(room);
    }

    public void addSeatsToRoom(UUID roomId,
                                  int size,
                                  String row,
                                  String type,
                                  Set<Integer> skipNumbers) {

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        if (row == null || row.isEmpty()) {
            throw new IllegalArgumentException("Row cannot be null or empty");
        }

        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }

        if (skipNumbers == null) {
            throw new IllegalArgumentException("Skip numbers cannot be null");
        }

        Room room = getRoomById(roomId);

        if (room == null) {
            throw new IllegalArgumentException("Room with id " + roomId + " not found");
        }

        room.addRowOfSeats(size, row, type, skipNumbers);
    }

    public void addSeatsToRoom(UUID roomId,
                               int size,
                               String row,
                               String type) {

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        if (row == null || row.isEmpty()) {
            throw new IllegalArgumentException("Row cannot be null or empty");
        }

        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }

        Room room = getRoomById(roomId);

        if (room == null) {
            throw new IllegalArgumentException("Room with id " + roomId + " not found");
        }

        room.addRowOfSeats(size, row, type);
    }

    public void createShowInRoom(UUID roomId,
                                 UUID movieId,
                                 String showTimeInput) {

        Room room = getRoomById(roomId);

        if (room == null) {
            throw new IllegalArgumentException("Room with id " + roomId + " not found");
        }

        Movie movie = getMovieById(movieId);

        if (movie == null) {
            throw new IllegalArgumentException("Movie with id " + movieId + " not found");
        }

        if (showTimeInput == null || showTimeInput.isEmpty()) {
            throw new IllegalArgumentException("Show time cannot be null or empty");
        }

        //Transform the string to LocalDateTime, if the format is not correct, it will throw an exception
        LocalDateTime showTime = LocalDateTime.parse(showTimeInput);

        room.createShow(showTime, movie);
    }

    public void addMovie(
        String title,
        String description,
        String genre,
        String director,
        int durationInMinutes,
        String releaseDate,
        String language
    ) {

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }

        if (director == null || director.isEmpty()) {
            throw new IllegalArgumentException("Director cannot be null or empty");
        }

        if (durationInMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }

        if (releaseDate == null || releaseDate.isEmpty()) {
            throw new IllegalArgumentException("Release date cannot be null or empty");
        }

        if (language == null || language.isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }

        Movie movie = new Movie(
            title,
            description,
            genre,
            director,
            durationInMinutes,
            releaseDate,
            language
        );

        movies.add(movie);
    }

    public Movie getMovieById(UUID movieId) {
        for (Movie movie : movies) {
            if (movie.getId().equals(movieId)) {
                return movie;
            }
        }
        return null;
    }

    public Room getRoomById(UUID roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        customers.add(customer);
    }

    public Customer getCustomerById(UUID customerId) {
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public Room getRoomOfShow(UUID showId) {
        for (Room room : rooms) {
            if (room.getShowById(showId) != null) {
                return room;
            }
        }
        return null;
    }

    public Ticket bookTicket(UUID customerId, 
        UUID showId, 
        Set<String> seatIdentifiers,
        String paymentSystemName) {
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        // Find the show
        Show show = getShowById(showId);


        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }

        Room room = getRoomOfShow(showId);

        // Book seats
        Set<Seat> bookedSeats = room.bookSeats(seatIdentifiers);

        // Calculate price
        long totalPrice = calculatePrice(customer, bookedSeats);
        
        PaymentSystem paymentSystem = paymentSystems.getOrDefault(
            paymentSystemName,
            paymentSystems.get("MockPaymentSystem")
        );

        // Process payment
        boolean paymentSuccessful = paymentSystem.processPayment(customer, totalPrice);

        if (!paymentSuccessful) {
            // Rollback booked seats
            room.releaseSeats(bookedSeats);

            throw new RuntimeException("Payment failed");
        }
        
        // Create ticket
        Ticket ticket = new Ticket(bookedSeats, show, customer);
        tickets.add(ticket);
        customer.addTicket(ticket);
        
        return ticket;
    }

    private Show getShowById(UUID showId) {
        for (Room room : rooms) {
            if(room.getShowById(showId) != null) {
                return room.getShowById(showId);
            }
        }

        return null;
    }

    public void cancelTicket(UUID ticketId) {
        Ticket ticketToCancel = null;
        for (Ticket ticket : tickets) {
            if (ticket.getId().equals(ticketId)) {
                ticketToCancel = ticket;
                break;
            }
        }
        
        if (ticketToCancel != null) {
            // Release seats
            for (Seat seat : ticketToCancel.getSeats()) {
                seat.setStatus(Seat.AVAILABLE);
            }
            
            // Remove ticket from list
            tickets.remove(ticketToCancel);
            
            // Remove from customer's list if needed
            for (Customer customer : customers) {
                customer.getTickets().remove(ticketToCancel);
            }
        }
    }

    private long calculatePrice(Customer customer, Set<Seat> seats) {
        long basePrice = 0;
        for (Seat seat : seats) {
            basePrice += seat.getPrice();
        }
        
        double discount = customer.getDiscount();
        return Math.round(basePrice * (1 - discount));
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
}
