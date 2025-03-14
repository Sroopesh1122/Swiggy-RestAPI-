package com.shivu.swiggy_api.repository;


import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.shivu.swiggy_api.entity.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

	@Query("{user.userId:?0}")
	public List<Cart> findCartsByUserId(@Param("id") String userId);
	
	@Query(value = "{ 'user.userId': ?0 }",count = true)
	public Integer getCartsCountByUserId(@Param("id") String userId);
	
	//To check wheather the item is saved in cart or not
	
	 @Query(value = "{ 'user.userId': ?0, 'menuItem.itemId': ?1 }", exists = true)
	 boolean existsByUserIdAndItemId(String userId, String itemId);
	
	 void deleteByUserUserId(String userId);

}
