package org.javeriana.payment;

import org.javeriana.model.user.customer.Customer;

public interface PaymentSystem {

    boolean processPayment(Customer customer, long amount);

}
