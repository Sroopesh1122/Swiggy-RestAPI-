package com.shivu.swiggy_api.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Document
public class Restaurant {
	
	@Id
	private String restaurantId;
	
	private String name;
	
	private String address;
	
	private String phoneNumber;
	
	private String password;
	
	private String email;
	
	private Double rating;
	
	private Integer reviewsCount;
	
private String passwordResetToken;
	
	private LocalDateTime passwordExpiredBy;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@JsonIgnore //To ignore this field from being converting to json\
	@DBRef
	private List<MenuItem> menuItems;
	
}
