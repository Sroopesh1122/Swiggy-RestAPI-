package com.shivu.swiggy_api.dto;


import lombok.Data;

@Data
public class CartDTO 
{
	private String cart_id;
	
	private String user_id;
	
	private String menuItem_id;
}
