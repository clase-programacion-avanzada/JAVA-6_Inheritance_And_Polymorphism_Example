package org.javeriana.payment;

import java.io.Serializable;
import org.javeriana.model.user.customer.Customer;

public class MockPaymentSystem implements PaymentSystem, Serializable {
    
    @Override
    public boolean processPayment(Customer customer, long amount) {
        // In a real system, this would connect to a payment processor
        System.out.println("Processing payment of $" + amount + " for customer " + customer.getName());
        return true; // Always successful for demo purposes
    }


}
