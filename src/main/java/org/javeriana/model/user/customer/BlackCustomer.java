package org.javeriana.model.user.customer;

public class BlackCustomer extends Customer {


    public BlackCustomer(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public double getDiscount() {
        return 0.80;
    }
}
