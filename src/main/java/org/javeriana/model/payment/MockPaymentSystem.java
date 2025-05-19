package org.javeriana.model.payment;

import java.io.Serializable;
import org.javeriana.model.user.customer.Customer;

public class MockPaymentSystem implements PaymentSystem, Serializable {
    
    @Override
    public boolean processPayment(Customer customer, long amount) {
        
        return true; // Always successful for demo purposes
    }

    @Override
    public String getPaymentName() {
        return "Mock";
    }


}
