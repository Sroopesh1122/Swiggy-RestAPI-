package com.shivu.swiggy_api.controllers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shivu.swiggy_api.entity.Restaurant;
import com.shivu.swiggy_api.exception.RestaurantException;
import com.shivu.swiggy_api.services.IRestaurantService;
import com.shivu.swiggy_api.services.RestaurantDetails;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
	
	@Autowired
	private  IRestaurantService restaurantService;

	
	@GetMapping("/public/top-rated")
	public List<Restaurant> topFiveRestaurant(){
		return restaurantService.getTopFiveRestaurants();
	}
	
	
	
	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('RESTAURANT')")
	public ResponseEntity<?> getRestaurantById(@AuthenticationPrincipal RestaurantDetails restaurantDetails)
	{
	   
	   Restaurant restaurant = restaurantDetails.getUser();
	   return ResponseEntity.ok(restaurant);
	}
	
	
	@GetMapping("/public/{id}")
	public ResponseEntity<?> getRestaurantById(@PathVariable("id") Integer rId)
	{
		
		Restaurant restaurant = restaurantService.findById(rId);
		if(restaurant == null)
		{
			throw new RestaurantException("Restaurant not found");
		}
		restaurant.setPassword(null);
		return ResponseEntity.ok(restaurant);
	}
	

}
