package com.shivu.swiggy_api.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shivu.swiggy_api.entity.Order;

public interface IOrderService  {

	public Order createOrder(Order order);
	public Order updateOrder(Order order);
	public Order getOrderById(String id);
	public Page<Order> getAllOrdersByUserId(String userId, Pageable pageable);
	
	public Page<Order> getAllOrdersByRestaurant(String rId ,String filter, Pageable pageable);
	
	public Page<Order> getAllUndevlieredOrders(String a, Pageable pageable);
	
}
