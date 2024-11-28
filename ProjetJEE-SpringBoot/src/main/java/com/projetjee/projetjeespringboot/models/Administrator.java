package com.projetjee.projetjeespringboot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_administrator")
    private int idAdministrator;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public Administrator() {}

    // Getters et Setters
    public int getIdAdministrator() {
        return idAdministrator;
    }

    public void setIdAdministrator(int idAdministrator) {
        this.idAdministrator = idAdministrator;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
