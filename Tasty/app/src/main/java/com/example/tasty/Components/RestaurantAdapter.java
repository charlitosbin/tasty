package com.example.tasty.Components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tasty.Models.RestaurantModel;
import com.example.tasty.R;


import java.util.List;


public class RestaurantAdapter  extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{

    private List<RestaurantModel> restaurants;

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{

        TextView restaurantName;
        TextView restaurantAddress;
        TextView restaurantCountryAddress;

        RestaurantViewHolder(View itemView){
            super(itemView);

            setVariables(itemView);
        }

        private void setVariables(View itemView){
            restaurantName = (TextView)itemView.findViewById(R.id.restaurantNameTextView);
            restaurantAddress = (TextView)itemView.findViewById(R.id.restaurantAddressTextView);
            restaurantCountryAddress = (TextView)itemView.findViewById(R.id.restaurantCountryTextView);
        }
    }

    public RestaurantAdapter(List<RestaurantModel> restaurants){
        this.restaurants = restaurants;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurant,parent,false);
        RestaurantViewHolder rVh = new RestaurantViewHolder(v);

        return rVh;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        RestaurantModel restaurant = restaurants.get(position);
        String countryState = restaurant.getCountry() + ", " + restaurant.getState();

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantAddress.setText(restaurant.getDirection());
        holder.restaurantCountryAddress.setText(countryState);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}