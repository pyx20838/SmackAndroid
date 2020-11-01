package com.xmpp.smackchat.model;

public class User {
    private CharSequence username;
    private String password;

    public User(CharSequence username, String password) {
        this.username = username;
        this.password = password;
    }

    public CharSequence getUsername() {
        return username;
    }

    public void setUsername(CharSequence username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
