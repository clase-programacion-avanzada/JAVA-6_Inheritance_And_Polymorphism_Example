package org.javeriana.model.user;

import java.util.UUID;

public class AdminUser extends User {

    public AdminUser(UUID id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public AdminUser(String name, String email, String password) {
        super(name, email, password);
    }


}
