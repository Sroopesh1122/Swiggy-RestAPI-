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
public class DeliveryPartner 
{
  @Id
  private String partnerId;
  
  private String name;
  
  private String phoneNumber;
  
  private String vehicleDetails;
  
  @CreatedDate
  private LocalDateTime createdAt;
  
  private String email;
  
  private String password;
  
  private String passwordResetToken;
	
	private LocalDateTime passwordExpiredBy;
  
  @JsonIgnore
  @DBRef
  private List<Deliveries> deliveries ;
  
  @JsonIgnore
  @DBRef
  private List<Order> orders;
  
  
  
}
