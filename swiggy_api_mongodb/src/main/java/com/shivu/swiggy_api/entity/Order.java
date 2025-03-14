package com.shivu.swiggy_api.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
@Document
public class Order
{

	@Id
	private String orderId;
	
	private Double totalAmount;
	
	private String status;
	
	private String payMode;
	
	private String deliveryAddress;
	
	private Integer reviewed;
	
	private String razorpayId;
	
	
	private LocalDateTime createdAt;
	
	@JsonIgnore
	@DBRef
	private User user;
	
	@JsonIgnore
	@DBRef
	private Restaurant restaurant;
	
	@JsonIgnore
	@DBRef
	private List<OrderItem> orderItems;
	
	@JsonIgnore
	@DBRef
	private DeliveryPartner pickedBy;
}
