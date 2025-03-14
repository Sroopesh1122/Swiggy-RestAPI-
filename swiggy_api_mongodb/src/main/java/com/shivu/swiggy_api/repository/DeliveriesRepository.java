package com.shivu.swiggy_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shivu.swiggy_api.entity.Deliveries;

@Repository
public interface DeliveriesRepository extends MongoRepository<Deliveries, String> {

	@Query("{ 'deliveryPartner.partnerId' : ?0, " +
	           "  $or: [ { 'deliveryStatus' : ?1 }, { ?1 : null }, { ?1 : '' } ] }")
	    Page<Deliveries> findAllDeliveriesByPartnerId(String partnerId, String q, Pageable pageable);

	    @Query("{ 'order.orderId' : ?0 }")
	    Deliveries findDeliveryByOrderId(String orderId);
}
