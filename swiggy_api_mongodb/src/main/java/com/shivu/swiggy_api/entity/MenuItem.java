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
public class MenuItem {

	@Id
	private String itemId;

	private String name;

	private String description;

	private Double price;

	private Integer available;

	private String category;

	private String img;

	private Double rating;
	
	private Integer discount;

	private Integer reviewsCount;

	@CreatedDate
	private LocalDateTime createdAt;

	@JsonIgnore
	@DBRef
	private Restaurant restaurant;

}
