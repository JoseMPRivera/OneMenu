package com.aje.onemenu;

public class Restaurant {

    private String name,address,description,website;
    private int phone_number;

    public Restaurant(){

    }

    public Restaurant(String name, String address, String description, String website, int phone_number) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.website = website;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public int getPhone_number() {
        return phone_number;
    }
}
