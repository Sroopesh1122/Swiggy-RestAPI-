package com.shivu.swiggy_api.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shivu.swiggy_api.entity.MenuItem;
import com.shivu.swiggy_api.entity.Restaurant;
import com.shivu.swiggy_api.exception.RestaurantException;
import com.shivu.swiggy_api.request.MenuItemRequest;
import com.shivu.swiggy_api.request.MenuUpdateRequest;
import com.shivu.swiggy_api.response.SuggestionResponse;
import com.shivu.swiggy_api.services.CustomerDetails;
import com.shivu.swiggy_api.services.ICartService;
import com.shivu.swiggy_api.services.IMenuItemService;
import com.shivu.swiggy_api.services.IRestaurantService;
import com.shivu.swiggy_api.services.RestaurantDetails;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

	@Autowired
	private IRestaurantService restaurantService;

	@Autowired
	private IMenuItemService menuItemService;

	@Autowired
	private ICartService cartService;

	// Used by Restaurant
	@Transactional
	@PostMapping("/create")
	@PreAuthorize("hasRole('RESTAURANT')")
	public MenuItem menuitem(@AuthenticationPrincipal RestaurantDetails restaurantDetails,
			@RequestBody MenuItemRequest request) {

		

		Restaurant restaurant = restaurantService.findById(request.getRestaurantId());
		if (restaurant == null) {
			throw new RestaurantException("Restaurant not Found");
		}

		MenuItem menuItem = new MenuItem();
		menuItem.setAvailable(1);
		String taggedCategory = Arrays.asList(request.getCategory().split(",")).stream().map(obj -> "#".concat(obj))
				.collect(Collectors.joining(","));
		menuItem.setCategory(taggedCategory);
		menuItem.setCreatedAt(LocalDateTime.now());
		menuItem.setDescription(request.getDescription());
		menuItem.setDiscount(request.getDiscount());
		menuItem.setImg(request.getImage());
		menuItem.setName(request.getName());
		menuItem.setPrice(request.getPrice());
		menuItem.setRating(0.0);
		menuItem.setReviewsCount(0);
		menuItem.setRestaurant(restaurantDetails.getUser());
		MenuItem createdMenu = menuItemService.create(menuItem);
		return createdMenu;
	}

	// Used by restaurant

	@GetMapping("/restaurant/menu-list")
	@PreAuthorize("hasRole('RESTAURANT')")
	public Page<MenuItem> getAllRestaurantItem(
			@AuthenticationPrincipal RestaurantDetails restaurantDetails,
			@RequestParam Integer restaurantId, 
			@RequestParam(required = false) String q,
			@RequestParam(required = false) Integer page, 
			@RequestParam(required = false) Integer limit) {


		page = page != null ? page : 1;
		limit = limit != null ? limit : 10;

		return menuItemService.findByRestaurant(restaurantId, q, page - 1, limit);

	}

	// used by restaurant
	@GetMapping("/restaurant/menu/{menuId}")
	@PreAuthorize("hasRole('RESTAURANT')")
	public MenuItem getMenuItem(
			@AuthenticationPrincipal RestaurantDetails restaurantDetails,
			@PathVariable Integer menuId) {
		
		return menuItemService.findById(menuId);
	}

	// used by restaurant
	@PutMapping("/update")
	@PreAuthorize("hasRole('RESTAURANT')")
	public ResponseEntity<?> updateMenuItem(@AuthenticationPrincipal RestaurantDetails restaurantDetails,
			@RequestBody MenuUpdateRequest request) {
		

		MenuItem menuItem = menuItemService.findById(request.getItemId());
		menuItem.setAvailable(request.getAvailable());
		String taggedCategory = Arrays.asList(request.getCategory().split(",")).stream().map(obj -> "#".concat(obj))
				.collect(Collectors.joining(","));
		menuItem.setCategory(taggedCategory);
		menuItem.setDescription(request.getDescription());
		menuItem.setDiscount(request.getDiscount());
		menuItem.setImg(request.getImage());
		menuItem.setName(request.getName());
		menuItem.setPrice(request.getPrice());

		menuItem = menuItemService.update(menuItem);

		Map<String, String> response = new HashMap<>();
		response.put("success", "true");
		response.put("message", "Food Item Updated Successfully!");

		return ResponseEntity.ok(response);
	}

	// All
	@GetMapping("/")
	public ResponseEntity<?> getMenuItems(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) Integer rId, 
			@RequestParam Integer page, @RequestParam Integer limit) {
		page = page - 1;
		return ResponseEntity.ok(menuItemService.findMenuItms(q, page, limit, rId));
	}

	@GetMapping("/suggestions")
	public ResponseEntity<?> getSuggestionData(@RequestParam(required = false) String filter) {

		Set<SuggestionResponse> suggestionData = menuItemService.getSuggestionData(filter);
		return ResponseEntity.ok(suggestionData);
	}

	// All
	@GetMapping("/food")
	public ResponseEntity<?> getMenuItemsBySearchText(
			@RequestParam(required = false, defaultValue = "") String q,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false) Integer rid,
			@RequestParam(required = false, defaultValue = "0") Integer rating,
			@RequestParam(required = false, defaultValue = "0-Infinity") String price, 
			@RequestParam Integer limit) {
		
		
		page = page - 1;

		String[] priceValue = price.split("-");
		Integer minPrice = Integer.parseInt(priceValue[0]);
		Integer maxPrice = priceValue[1].equals("Infinity") ? Integer.MAX_VALUE : Integer.parseInt(priceValue[1]);

		return ResponseEntity.ok(menuItemService.findMenuItems(q, page, rid, limit, minPrice, maxPrice, rating));
	}

	// All
	@GetMapping("/{id}")
	public ResponseEntity<?> getMenuItemWithRestaurantDetails(
		@AuthenticationPrincipal CustomerDetails customerDetails,	
		@PathVariable(name = "id") Integer itemId)
	{
		MenuItem menuItem = menuItemService.findById(itemId);
		Restaurant restaurant = restaurantService.findById(menuItem.getRestaurant().getRestaurantId());
		Map<String, Object> response = new HashMap<>();
		response.put("MenuItem", menuItem);
		response.put("restaurantId", restaurant.getRestaurantId());
		response.put("isSaved",customerDetails == null ? false : cartService.isSaved(customerDetails.getUser().getUserId(), itemId));
		return ResponseEntity.ok(response);
	}

	// All
	@GetMapping("/similar")
	public ResponseEntity<?> getSimilarItemByCategory(
			@RequestParam(name = "q" ,required = false,defaultValue = "") String searchText, 
			@RequestParam Integer page,
			@RequestParam Integer limit) {

		if (searchText.equals("")) {
			return ResponseEntity.ok(Page.empty());
		}

		page = page - 1;

		Pageable pageable = PageRequest.of(page, limit);
		List<String> keywords = Arrays.asList(searchText.split(","));
		String regex = keywords.stream().map(key -> '#' + Pattern.quote(key)).collect(Collectors.joining("|"));
		return ResponseEntity.ok(menuItemService.findSimilarItems(regex, pageable));
	}
}
