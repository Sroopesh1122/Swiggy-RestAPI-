package com.shivu.swiggy_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.shivu.swiggy_api.entity.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // ✅ Get Orders by User ID
    @Query("{ 'user.userId': ?0 }")
    Page<Order> getAllOrderByUserid(String userId, Pageable pageable);

    // ✅ Get Orders by Restaurant ID with Status Filter
    @Query("{ $and: [ " +
           "  { 'restaurant.restaurantId': ?0 }, " +
           "  { $or: [ { 'status': null }, { 'status': '' }, { 'status': ?1 } ] } " +
           "] }")
    Page<Order> getAllOrderByRestaurantId(String restaurantId, String filter, Pageable pageable);

    // ✅ Get Orders Not Delivered (Prepared Orders Without Assigned Delivery)
    @Query("{ $and: [ " +
           "  { 'status': 'prepared' }, " +
           "  { 'pickedBy': null }, " +
           "  { $or: [ { '?0': null }, { '?0': '' }, { 'deliveryAddress': { $regex: '^?0', $options: 'i' } } ] } " +
           "] }")
    Page<Order> getAllOrdersNotDelivered(String filter, Pageable pageable);
}
