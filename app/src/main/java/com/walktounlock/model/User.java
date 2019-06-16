package com.walktounlock.model;

import java.io.Serializable;

/**
 * Created by M Umer Saleem on 9/6/2017.
 */

public class User implements Serializable{
    int id;
    String name,email,password,pin;

    public User() {
    }

    public User(String name, String email, String password, String pin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
