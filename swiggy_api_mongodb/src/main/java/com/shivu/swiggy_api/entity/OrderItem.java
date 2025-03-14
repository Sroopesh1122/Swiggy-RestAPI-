package com.shivu.swiggy_api.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document
public class OrderItem 
{
	@Id
	private String orderItemId;

	private Integer quantity;

	private Double price;
	
	@JsonIgnore
	@DBRef
	private Order order;
	
	@JsonIgnore
	@DBRef
	private MenuItem menuItem;
}
