package com.example.moments.util;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String telephoneNumber;
    private String address;

    public User(String username, String password, String name, String surname, String telephoneNumber, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.surname = surname;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
