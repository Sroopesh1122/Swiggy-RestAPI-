package com.shivu.swiggy_api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.shivu.swiggy_api.entity.Restaurant;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    
    // ✅ Find a restaurant by email
    Optional<Restaurant> findByEmail(String email);

    // ✅ Get Top 5 Restaurants sorted by rating in descending order
    @Query(value = "{}",sort = "{ 'rating': -1 }")
    List<Restaurant> topFiveRestaurant();
    
    
    public Optional<Restaurant>  findByPasswordResetToken(String passwordResetToken);
}
