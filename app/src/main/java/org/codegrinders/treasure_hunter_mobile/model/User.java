package org.codegrinders.treasure_hunter_mobile.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String email;
    private String username;
    private String password;
    private int points;
    private boolean hasWon;

    public User(String id, String username, int points) {
        this.id = id;
        this.username = username;
        this.points = points;
    }

    public User(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }
}