package com.example.projectbit.Models;

public class WasmachineProfile {
    String name;
    String type;
    String maxCapacity;
    String documentID;

    public WasmachineProfile() {
    }

    public WasmachineProfile(String name, String type, String maxCapacity) {
        this.name = name;
        this.type = type;
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
