package com.shivu.swiggy_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shivu.swiggy_api.entity.OrderItem;
import com.shivu.swiggy_api.repository.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements IOrderItemService {

	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	public OrderItem add(OrderItem orderItem) {
		// TODO Auto-generated method stub
		return orderItemRepository.save(orderItem);
	}

	@Override
	public OrderItem update(OrderItem orderItem) {
		// TODO Auto-generated method stub
		return orderItemRepository.save(orderItem);
	}

	@Override
	public OrderItem getById(Integer id) {
		// TODO Auto-generated method stub
		return orderItemRepository.findById(id).get();
	}

}
