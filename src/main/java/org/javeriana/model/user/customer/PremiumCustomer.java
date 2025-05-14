package org.javeriana.model.user.customer;

import java.util.UUID;

public class PremiumCustomer extends Customer {
    
    private static final double DISCOUNT_PERCENTAGE = 0.20; // 20% discount

    public PremiumCustomer(UUID id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public PremiumCustomer(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public double getDiscount() {
        return DISCOUNT_PERCENTAGE;
    }
}
