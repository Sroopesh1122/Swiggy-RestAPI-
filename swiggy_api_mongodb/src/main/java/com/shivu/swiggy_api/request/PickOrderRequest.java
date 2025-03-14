package com.shivu.swiggy_api.request;

import lombok.Data;

@Data
public class PickOrderRequest
{
   private String orderId;
   private String deliveryPartnerId;
}
