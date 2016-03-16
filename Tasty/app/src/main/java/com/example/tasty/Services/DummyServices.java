package com.example.tasty.Services;


import com.example.tasty.Models.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class DummyServices {

    public List<RestaurantModel> GetRestaurants(String type){
        List<RestaurantModel> restaurantsList = new ArrayList<>();

        restaurantsList.add(new RestaurantModel(
                "Paulo Maggiani's di colombo",
                "44510",
                "Jalisco",
                "Guadalajara",
                "Av de Las Rosas 2830-A, Chapalita",
                20.6649294,
                -103.3942613
        ));

        restaurantsList.add(new RestaurantModel(
                "La Flor de Calabaza",
                "44490",
                "Jalisco",
                "Guadalajara",
                "Av. Mariano Otero 3261, Verde Valle",
                20.6599995,
                -103.3942857
        ));

        restaurantsList.add(new RestaurantModel(
                "La Casa del Waffle",
                "44520",
                "Jalisco",
                "Guadalajara",
                "Calle San José 1933, Chapalita",
                20.6635223,
                -103.4622748
        ));


        restaurantsList.add(new RestaurantModel(
                "La Chata",
                "44100",
                "Jalisco",
                "Guadalajara",
                "Av. Corona 126, Centro",
                20.6747042,
                -103.4166297
        ));

        restaurantsList.add(new RestaurantModel(
                "Suehiro",
                "44190",
                "Jalisco",
                "Guadalajara",
                "Avenida de la Paz 1701, Moderna",
                20.6708599,
                -103.4330082
        ));

        return restaurantsList;
    }
}
