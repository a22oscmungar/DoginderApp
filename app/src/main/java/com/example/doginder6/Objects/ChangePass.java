package com.example.doginder6.Objects;

public class ChangePass {
    public String mail, pass;

    public ChangePass(String mail, String pass) {
        this.mail = mail;
        this.pass = pass;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {

        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "ChangePass{" +
                "mail='" + mail + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
