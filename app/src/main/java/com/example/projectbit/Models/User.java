package com.example.projectbit.Models;

import java.io.Serializable;

public class User implements Serializable {
    String naam;
    String email;
    String phoneNumber;
    String adres;
    String profileImg;
    String wasmachine;
    String idArduino;


    public User(String email, String idArduino) {
        this.email = email;
        this.idArduino = idArduino;
    }

    public User(){

    }

    public String getNaam() {
        return naam;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdres() {
        return adres;
    }

    public String getEmail() {
        return email;
    }

    public String getWasmachine() {
        return wasmachine;
    }

    public String getIdArduino() {
        return idArduino;
    }

}
