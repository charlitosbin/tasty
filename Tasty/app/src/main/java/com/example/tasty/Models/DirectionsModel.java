package com.example.tasty.Models;

import com.google.api.client.util.Key;

import java.util.List;

public  class DirectionsModel {

    @Key("routes")
    public List<Route> routes;

    public class Route{
        @Key("overview_polyline")
        public OverviewPolyLine overviewPolyline;
    }

    public class OverviewPolyLine {
        @Key("points")
        public String points;
    }
}

