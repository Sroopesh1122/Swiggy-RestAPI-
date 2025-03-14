package com.shivu.swiggy_api.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@Document
public class User {

	@Id
	private String userId;

	private String name;

	
	private String email;

	private String phoneNumber;

	private String address;

	private String password;

	@CreatedDate
	private LocalDateTime createdAt;

	@JsonIgnore
	@DBRef
	private List<Cart> cartItems;
	
private String passwordResetToken;
	
	private LocalDateTime passwordExpiredBy;
	

}
