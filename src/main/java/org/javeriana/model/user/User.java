package org.javeriana.model.user;

import java.util.UUID;

public abstract class User {
    protected final UUID id;
    protected String name;
    protected String email;
    protected String password;

    public User(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this(UUID.randomUUID(), name, email, password);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
