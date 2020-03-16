package com.example.todoapp.models;

import androidx.room.Entity;

import java.io.Serializable;

@Entity
public class Users implements Serializable {
    private String title;
    private String email;

    public Users(String title, String email) {
        this.title = title;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
