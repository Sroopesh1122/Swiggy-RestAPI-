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
public class Deliveries {
	@Id
	private String deliveryId;

	private String deliveryStatus;

	@CreatedDate
	private LocalDateTime assignedAt;

	private LocalDateTime deliveredAt;

	private String deliver_code;

	@JsonIgnore
	@DBRef
	private Order order;

	@JsonIgnore
	@DBRef
	private DeliveryPartner deliveryPartner;

}
