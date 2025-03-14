package com.shivu.swiggy_api.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.shivu.swiggy_api.entity.OrderItem;

@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {

    // ✅ Find all OrderItems by orderId
    @Query("{ 'order.orderId' : ?0 }")
    List<OrderItem> findByOrderId(String orderId);
}
