package com.example.tasty.Models;


import com.google.api.client.util.Key;

import java.util.List;

public class PlacesModel {
    @Key("results")
    public List<Result> results;

    public static class Result{
        @Key("geometry")
        public Geometry geometry;

        @Key("name")
        public String name;

        @Key("formatted_address")
        public String formattedAddress;

        @Key("rating")
        public double rating;
    }

    public static class Geometry {
        @Key("location")
        public Location location;
    }

    public static class Location {
        @Key("lat")
        public double lat;

        @Key("lng")
        public double lng;
    }
}
