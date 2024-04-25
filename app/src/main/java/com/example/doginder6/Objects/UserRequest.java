package com.example.doginder6.Objects;

public class UserRequest {

    public String mailUsu;
    public String passUsu;

    public UserRequest(String mailUsu, String passUsu) {
        this.mailUsu = mailUsu;
        this.passUsu = passUsu;
    }

    public String getMailUsu() {
        return mailUsu;
    }

    public void setMailUsu(String mailUsu) {
        this.mailUsu = mailUsu;
    }

    public String getPassUsu() {
        return passUsu;
    }

    public void setPassUsu(String passUsu) {
        this.passUsu = passUsu;
    }
}
