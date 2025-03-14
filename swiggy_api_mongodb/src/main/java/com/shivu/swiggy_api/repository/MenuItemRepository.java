package com.shivu.swiggy_api.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shivu.swiggy_api.entity.MenuItem;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    // ✅ Search menu items by name or category, sorted by rating & restaurantId
    @Query("{ $and: [ " +
            " { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'category': { $regex: ?0, $options: 'i' } } ] }, " +
            " { $or: [ { 'restaurant.restaurantId': null }, { 'restaurant.restaurantId': 0 }, { 'restaurant.restaurantId': ?1 } ] } " +
            "] }")
    Page<MenuItem> findMenuItems(String q, String rid, Pageable pageable);

    // ✅ Search menu items where name starts with `q` or category contains `q`
    @Query("{ $or: [ { 'name': { $regex: '^?0', $options: 'i' } }, { 'category': { $regex: '?0', $options: 'i' } } ] }")
    List<MenuItem> searchText(String filter);

    // ✅ Filter menu items by name, category, restaurant, price range, and rating
    @Query("{ $and: [ " +
            " { $or: [ { '?0': null }, { '?0': '' }, { 'name': { $regex: '^?0', $options: 'i' } }, { 'category': { $regex: '?0', $options: 'i' } } ] }, " +
            " { $or: [ { '?1': 0 }, { '?1': null }, { 'restaurant.restaurantId': ?1 } ] }, " +
            " { $or: [ { '?2': 0 }, { 'price': { $gte: ?2 } } ] }, " +
            " { $or: [ { '?3': 0 }, { 'price': { $lte: ?3 } } ] }, " +
            " { $or: [ { '?4': 0 }, { 'rating': { $gte: ?4 } } ] } " +
            "] }")
    Page<MenuItem> getMenuItems(String filter, String rId, Integer minPrice, Integer maxPrice, Integer rating, Pageable pageable);

    // ✅ Find menu items where category matches regex pattern
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    Page<MenuItem> findByCategorysMatching(String regex, Pageable pageable);
}
