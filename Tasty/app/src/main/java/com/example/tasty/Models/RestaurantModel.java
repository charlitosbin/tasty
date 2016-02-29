package com.example.tasty.Models;

public class RestaurantModel {
    private double latitude;
    private double longitude;

    private String name;
    private int zipCode;
    private String state;
    private String direction;

    private EnumRestaurants restaurantType;

    public RestaurantModel(double latitude, double longitude, String name, int zipCode, String state, String direction) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.zipCode = zipCode;
        this.state = state;
        this.direction = direction;
    }

    public double GetLatitude(){
        return latitude;
    }

    public void SetLatitude(double latitude){
        this.latitude = latitude;
    }

    public double GetLongitude(){
        return longitude;
    }

    public void SetLongitude(double longitude){
        this.longitude = longitude;
    }

    public String GetName(){
        return name;
    }

    public void SetName(String name){
        this.name = name;
    }

    public int GetZipCode(){
        return zipCode;
    }

    public void SetZipCode(int zipCode){
        this.zipCode = zipCode;
    }

    public String GetState(){
        return state;
    }

    public void SetState(String state){
        this.state = state;
    }

    public String GetDirection(){
        return direction;
    }

    public void SetDirection(String direction){
        this.direction = direction;
    }

    public EnumRestaurants GetRestaurantType()
    {
        return restaurantType;
    }

    public void SetRestaurantType(EnumRestaurants restaurantType){
        this.restaurantType = restaurantType;
    }
}