package com.example.projectbit.Wasmachine;


import java.io.Serializable;

public class Wasmachine implements Serializable {
    String name;
    String type;
    int maxCapacity;
    String IDWasmachine;

    public Wasmachine(){

    }

    public Wasmachine(String name, String type, int maxCapacity, String IDWasmachine ) {
        this.name = name;
        this.type = type;
        this.maxCapacity = maxCapacity;
        this.IDWasmachine = IDWasmachine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public String getIDWasmachine() {
        return IDWasmachine;
    }



}
