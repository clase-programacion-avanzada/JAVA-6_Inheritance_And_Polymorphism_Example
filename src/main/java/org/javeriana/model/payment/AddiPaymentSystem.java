package org.javeriana.payment;

import org.javeriana.model.user.customer.Customer;

public class Addi implements PaymentSystem {

    @Override
    public boolean processPayment(Customer customer, long amount) {
        return true;
    }

    @Override
    public String getPaymentName() {
        return "Addi";
    }
}
