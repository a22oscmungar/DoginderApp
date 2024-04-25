package com.example.doginder6;

public class User {
    public int idUser;
    public String name;

    public User(int idUser, String name) {
        this.idUser = idUser;
        this.name = name;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
