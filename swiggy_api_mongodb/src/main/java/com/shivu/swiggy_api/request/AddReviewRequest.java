package com.shivu.swiggy_api.request;

import lombok.Data;

@Data
public class AddReviewRequest 
{
    private Integer rating;
	private String comment;
	private String itemId;
	private String orderId;
	private String userId;
}
