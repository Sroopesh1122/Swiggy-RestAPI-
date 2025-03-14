package com.shivu.swiggy_api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
@Document
public class Cart {

	@Id
	private String cartId;

	@DBRef
	private User user;

	@DBRef
	private MenuItem menuItem;

}
