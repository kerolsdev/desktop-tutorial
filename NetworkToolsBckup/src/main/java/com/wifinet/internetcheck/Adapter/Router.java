package com.wifinet.internetcheck.Adapter;

public class Router {

    String brand ;
    String username;
    String password;
    String model ;



    public Router (String brand , String model , String username , String password) {
        this.brand = brand;
        this.model = model;
        this.password = password;
        this.username = username;

    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
