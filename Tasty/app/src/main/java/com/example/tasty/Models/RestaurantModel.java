package com.example.tasty.Models;

public class RestaurantModel {

    private double longitude;
    private double latitude;

    private String name;
    private int zipCode;
    private String state;
    private String country;
    private String direction;

    private EnumRestaurants restaurantType;

    public RestaurantModel(String name, int zipCode, String state, String country, String direction) {
        this.name = name;
        this.zipCode = zipCode;
        this.state = state;
        this.direction = direction;

    }

    public RestaurantModel(String name, int zipCode, String state, String country, String direction,
                           double latitude, double longitude) {
        this.name = name;
        this.zipCode = zipCode;
        this.state = state;
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RestaurantModel(String name, String state, String country, String direction)
    {
        this.name = name;
        this.state = state;
        this.country = country;
        this.direction = direction;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getZipCode(){
        return zipCode;
    }

    public void setZipCode(int zipCode){
        this.zipCode = zipCode;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getDirection(){
        return direction;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setDirection(String direction){
        this.direction = direction;
    }

    public EnumRestaurants getRestaurantType()
    {
        return restaurantType;
    }

    public void setRestaurantType(EnumRestaurants restaurantType){
        this.restaurantType = restaurantType;
    }
}