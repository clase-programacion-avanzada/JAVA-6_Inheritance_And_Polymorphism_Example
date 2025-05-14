package org.javeriana.model.user.customer;

import java.util.UUID;

public class RegularCustomer extends Customer {

    public RegularCustomer(UUID id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public RegularCustomer(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public double getDiscount() {
        // Regular customers get no discount
        return 0.0;
    }
}
