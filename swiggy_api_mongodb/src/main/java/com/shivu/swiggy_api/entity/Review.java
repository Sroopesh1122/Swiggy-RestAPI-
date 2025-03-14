package com.shivu.swiggy_api.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document
public class Review {
	
	@Id
	private String reviewId;
	
	private Integer rating;
	
	private String comment;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@JsonIgnore
	@DBRef
	private MenuItem menuItem;
	
	@JsonIgnore
	@DBRef
	private  User user;
	
	@JsonIgnore
	@DBRef
	private Restaurant restaurant;

}
