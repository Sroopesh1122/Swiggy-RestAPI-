package com.shivu.swiggy_api.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shivu.swiggy_api.entity.Deliveries;

public interface IDeliveriesServices{

	public Deliveries create (Deliveries delivery);
	
	public Deliveries update(Deliveries delivery);
	
	public Deliveries getById(String deliveryId);
	
	public void deleteById(String deliveryId);
	
	public Page<Deliveries> getDeliveriesByDeliveryPartnerId(String deliveryPartnerId,String q ,Pageable pageable);
	
	public Deliveries getByOrderId(String orderId);
	
	
}
