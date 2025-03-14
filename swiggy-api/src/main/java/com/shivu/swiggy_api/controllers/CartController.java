package com.shivu.swiggy_api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shivu.swiggy_api.entity.Cart;
import com.shivu.swiggy_api.entity.MenuItem;
import com.shivu.swiggy_api.entity.User;
import com.shivu.swiggy_api.exception.CartException;
import com.shivu.swiggy_api.request.CartAddRequest;
import com.shivu.swiggy_api.services.CustomerDetails;
import com.shivu.swiggy_api.services.ICartService;
import com.shivu.swiggy_api.services.IMenuItemService;
import com.shivu.swiggy_api.services.IUserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	private final Integer CART_MAX_LIMIT = 25;

	@Autowired
	private ICartService cartService;

	@Autowired
	private IMenuItemService menuItemService;
	
	@Autowired
	private IUserService userService;

	@PostMapping("/add")
	@PreAuthorize("hasRole('CUSTOMER')") // if other roles give 403 forbidden error 
	public ResponseEntity<?> addToCart(
			@AuthenticationPrincipal CustomerDetails customerDetails,
			@RequestBody CartAddRequest request) {
		
		Map<String, String> response = new HashMap<>();

		if (request.getMenuId() != null && request.getMenuId() != 0) {
			
			User user = userService.findById(request.getUserId());
			
			MenuItem menuItem = menuItemService.findById(request.getMenuId());
			
			
			if (cartService.isSaved(user.getUserId(), request.getMenuId())) {
				throw new CartException("ALready exists in cart");
			}
			
			if(user ==null || menuItem ==null)
			{
				throw new CartException("Failed to add to cart");
			}
	
			if (cartService.getCartItemCount(user.getUserId()) >= CART_MAX_LIMIT) {
				throw new CartException("Reached Maximum Limit,Remove items from cart to add");
			}

			Cart cart = new Cart();
			cart.setUser(user);
            cart.setMenuItem(menuItem);
			cartService.addToCart(cart);
			response.put("status", "success");
			response.put("message", "Item added tomcart Successfully");

		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> getAllCartByUser(
			@AuthenticationPrincipal CustomerDetails customerDetails , 
			@RequestParam Integer userId) {
		
		
		User user = userService.findById(userId);
		
		if(user ==null)
		{
			throw new CartException("User Not Found");
		}
		
		List<Cart> cartItems = cartService.allCartItemsByUserId(user.getUserId());
		
		return ResponseEntity.ok(cartItems);

	}

	@DeleteMapping("/")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> deleteItemFromCart(
			@AuthenticationPrincipal CustomerDetails customerDetails ,
			@RequestParam Integer cartId)
	{
		Map<String, String> response =  new HashMap<>();
		
		Cart cart = cartService.getById(cartId);
		if(cart ==null)
		{
			throw new CartException("Cart Item Not Found");
		}
		
		cartService.deleteCartItem(cartId);
		response.put("status", "success");
		response.put("message","Item removed from cart successfully");
		return ResponseEntity.ok(response);
	}

}
