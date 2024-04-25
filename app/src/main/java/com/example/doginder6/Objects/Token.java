package com.example.doginder6;

public class Token {
    public String token, mail;

    public Token(String token, String mail) {
        this.token = token;
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
