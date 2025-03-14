package com.shivu.swiggy_api.services;

import java.util.List;

import com.shivu.swiggy_api.entity.Restaurant;

public interface IRestaurantService 
{
  public Restaurant createRestaurant(Restaurant restaurant);
  public Restaurant upadetRestaurant(Restaurant restaurant);
  public Restaurant findById(Integer restaurantId);
  public Restaurant findByEmail(String email);
  public List<Restaurant> findAll();
  public List<Restaurant> getTopFiveRestaurants();
  public Restaurant findByPasswordResetToken(String token);
}
