package com.shivu.swiggy_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shivu.swiggy_api.entity.Cart;
import com.shivu.swiggy_api.repository.CartRepository;

@Service
public class CartServiceImpl implements ICartService {

	
	@Autowired
	private CartRepository cartRepository;
	
	@Override
	public Cart addToCart(Cart cart) {
		
		return cartRepository.save(cart);
	}

	@Override
	public void deleteCartItem(String cartId) {
	
		 cartRepository.deleteById(cartId);

	}

	@Override
	public List<Cart> allCartItemsByUserId(String userId) {
		return cartRepository.findCartsByUserId(userId);
	}
	
	@Override
	public boolean isSaved(String userId, String itemId) {
		return cartRepository.existsByUserIdAndItemId(userId,itemId);
	}
	
	@Override
	public Integer getCartItemCount(String userId) {
		return cartRepository.getCartsCountByUserId(userId);
	}
	
	@Override
	public Integer deleteCartItemsByUserId(String userId) {
		// TODO Auto-generated method stub
		 cartRepository.deleteByUserUserId(userId);
		 return 1;
	}

}
