package com.shivu.swiggy_api.services;

import java.util.List;

import com.shivu.swiggy_api.entity.Cart;

public interface ICartService {
	public Cart addToCart(Cart cart);

	public void deleteCartItem(String cartId);

	public List<Cart> allCartItemsByUserId(String userId);

	public boolean isSaved(String userId, String itemId);

	public Integer getCartItemCount(String userId);

	public Integer deleteCartItemsByUserId(String userId);

}
