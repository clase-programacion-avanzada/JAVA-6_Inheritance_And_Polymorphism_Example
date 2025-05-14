package org.javeriana.model.user.customer;

import java.util.UUID;

public class GoldCustomer extends Customer {
    
    private static final double DISCOUNT_PERCENTAGE = 0.10; // 10% discount

    public GoldCustomer(UUID id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public GoldCustomer(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public double getDiscount() {
        return DISCOUNT_PERCENTAGE;
    }
}
