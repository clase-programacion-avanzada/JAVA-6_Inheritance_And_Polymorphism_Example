package org.javeriana;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.javeriana.model.Cinema;
import org.javeriana.model.Movie;
import org.javeriana.model.Room;
import org.javeriana.model.Show;
import org.javeriana.model.Ticket;
import org.javeriana.model.user.customer.Customer;
import org.javeriana.model.user.customer.GoldCustomer;
import org.javeriana.model.user.customer.PremiumCustomer;
import org.javeriana.model.user.customer.RegularCustomer;
import org.javeriana.payment.MockPaymentSystem;
import org.javeriana.payment.PaymentSystem;

public class Main {
    public static void main(String[] args) {
        // Initialize payment system
        PaymentSystem paymentSystem = new MockPaymentSystem();
        
        // Create cinema
        Cinema cinema = new Cinema(paymentSystem);
        
        // Create a room
        cinema.createRoom("Room 1");
        Room room = cinema.getRoomById(cinema.getRooms().getFirst().getId());
        
        // Add seats to room
        Set<Integer> skipNumbers = new HashSet<>();
        skipNumbers.add(10);
        skipNumbers.add(5);

        cinema.addSeatsToRoom(room.getId(), 10, "A", "Regular", skipNumbers);
        cinema.addSeatsToRoom(room.getId(), 10, "B", "Regular");
        cinema.addSeatsToRoom(room.getId(), 10, "C", "Regular");
        cinema.addSeatsToRoom(room.getId(), 10, "D", "Regular");
        cinema.addSeatsToRoom(room.getId(), 10, "E", "Premium");
        cinema.addSeatsToRoom(room.getId(), 10, "F", "Premium");
        
        // Create movie
        cinema.addMovie("The Matrix",
            "A computer hacker learns about the true nature of reality",
            "Sci-Fi",
            "The Wachowskis",
            136,
            "1999-03-31",
            "English");
        Movie movie = cinema.getMovies().getFirst();
        
        // Create show
        LocalDateTime showTime =
            LocalDateTime.now()
                .plusDays(1)
                .withHour(20)
                .withMinute(0);

        cinema.createShowInRoom(room.getId(), movie.getId(), showTime.toString());

        //room.createShow(showTime, movie);
        Show show = room.getShows().getFirst();
        
        // Create customers of different types
        Customer regularCustomer = new RegularCustomer( "John Regular", "john@example.com", "password");
        Customer goldCustomer = new GoldCustomer( "Jane Gold", "jane@example.com", "password");
        Customer premiumCustomer = new PremiumCustomer( "Bob Premium", "bob@example.com", "password");
        
        cinema.addCustomer(regularCustomer);
        cinema.addCustomer(goldCustomer);
        cinema.addCustomer(premiumCustomer);
        
        // Book tickets for different customer types
        System.out.println("\n=== BOOKING TICKETS FOR DIFFERENT CUSTOMER TYPES ===\n");
        
        // Regular customer books seats A1, A2
        Set<String> regularSeats = new HashSet<>();
        regularSeats.add("A-1");
        regularSeats.add("A-2");
        Ticket regularTicket = cinema.bookTicket(regularCustomer.getId(), show.getId(), regularSeats);
        
        // Gold customer books seats B1, B2
        Set<String> goldSeats = new HashSet<>();
        goldSeats.add("B-1");
        goldSeats.add("B-2");
        Ticket goldTicket = cinema.bookTicket(goldCustomer.getId(), show.getId(), goldSeats);
        
        // Premium customer books premium seats E1, E2
        Set<String> premiumSeats = new HashSet<>();
        premiumSeats.add("E-1");
        premiumSeats.add("E-2");
        Ticket premiumTicket = cinema.bookTicket(premiumCustomer.getId(), show.getId(), premiumSeats);
        
        // Print ticket details
        System.out.println("\n=== REGULAR CUSTOMER TICKET ===");
        regularTicket.ticketDetails().forEach(System.out::println);
        
        System.out.println("\n=== GOLD CUSTOMER TICKET ===");
        goldTicket.ticketDetails().forEach(System.out::println);
        
        System.out.println("\n=== PREMIUM CUSTOMER TICKET ===");
        premiumTicket.ticketDetails().forEach(System.out::println);
        
        // Cancel a ticket
        System.out.println("\n=== CANCELLING A TICKET ===");
        cinema.cancelTicket(goldTicket.getId());
        System.out.println("Ticket cancelled successfully!");
        
        // Print available seats after bookings and cancellation
        System.out.println("\n=== AVAILABLE SEATS AFTER BOOKINGS ===");
        room.getAvailableSeats().forEach(seat -> 
            System.out.println(seat.getSeatDetails()));
    }
}