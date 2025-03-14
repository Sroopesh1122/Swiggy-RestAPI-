package com.shivu.swiggy_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.shivu.swiggy_api.entity.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    // ✅ Get all Reviews by Menu Item ID
    @Query("{ 'menuItem.itemId': ?0 }")
    Page<Review> getAllReviewsByItemId(String menuId, Pageable pageable);

    // ✅ Get all Reviews by Restaurant ID
    @Query("{ 'restaurant.restaurantId': ?0 }")
    Page<Review> getAllReviewsByRestaurantId(String rId, Pageable pageable);
}
