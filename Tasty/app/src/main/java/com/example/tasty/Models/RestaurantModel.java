package com.example.tasty.Models;

public class RestaurantModel {

    private double longitude;
    private double latitude;

    private String name;
    private String zipCode;
    private String state;
    private String country;
    private String address;
    private String city;

    private EnumRestaurants restaurantType;


    public RestaurantModel(){

    }

    public RestaurantModel(String name, String zipCode, String state, String country, String address) {
        this.name = name;
        this.zipCode = zipCode;
        this.state = state;
        this.address = address;

    }

    public RestaurantModel(String name, String zipCode, String state, String country, String address,
                           double latitude, double longitude) {
        this.name = name;
        this.zipCode = zipCode;
        this.state = state;
        this.country = country;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RestaurantModel(String name, String state, String country, String address)
    {
        this.name = name;
        this.state = state;
        this.country = country;
        this.address = address;
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

    public String getZipCode(){
        return zipCode;
    }

    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getAddress(){
        return address;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public EnumRestaurants getRestaurantType()
    {
        return restaurantType;
    }

    public void setRestaurantType(EnumRestaurants restaurantType){
        this.restaurantType = restaurantType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}