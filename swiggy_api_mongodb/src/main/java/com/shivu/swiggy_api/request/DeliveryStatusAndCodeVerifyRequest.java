package com.shivu.swiggy_api.request;

import lombok.Data;

@Data
public class DeliveryStatusAndCodeVerifyRequest {
	private String DeliveryId;
	private String deliveryCode;

}
