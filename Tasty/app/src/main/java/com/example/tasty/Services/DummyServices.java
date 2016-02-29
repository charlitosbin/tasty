package com.example.tasty.Services;


import com.example.tasty.Models.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class DummyServices {

    public List<RestaurantModel> GetRestaurants(String type){
        List<RestaurantModel> restaurantsList = new ArrayList<>();

        restaurantsList.add(new RestaurantModel(
                "Paulo Maggiani's di colombo",
                44510,
                "Jalisco",
                "Guadalajara",
                "Av de Las Rosas 2830-A, Chapalita"
        ));

        restaurantsList.add(new RestaurantModel(
                "La Flor de Calabaza",
                44490,
                "Jalisco",
                "Guadalajara",
                "Av. Mariano Otero 3261, Verde Valle"
        ));

        restaurantsList.add(new RestaurantModel(
                "La Casa del Waffle",
                44520,
                "Jalisco",
                "Guadalajara",
                "Calle San José 1933, Chapalita"
        ));


        restaurantsList.add(new RestaurantModel(
                "La Chata",
                44100,
                "Jalisco",
                "Guadalajara",
                "Av. Corona 126, Centro"
        ));

        restaurantsList.add(new RestaurantModel(
                "Suehiro",
                44190,
                "Jalisco",
                "Guadalajara",
                "Avenida de la Paz 1701, Moderna"
        ));

        return restaurantsList;
    }
}
