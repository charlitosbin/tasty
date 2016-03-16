package com.example.tasty.Models;

import com.google.api.client.util.Key;

import java.util.List;

public final class DirectionsModel {

    @Key("routes")
    public List<Route> routes;

    public static class Route{
        @Key("overview_polyline")
        public OverviewPolyLine overviewPolyline;
    }

    public static class OverviewPolyLine {
        @Key("points")
        public String points;
    }
}

