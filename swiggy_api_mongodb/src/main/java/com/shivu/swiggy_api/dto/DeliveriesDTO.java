package com.shivu.swiggy_api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DeliveriesDTO
{
  private String delivery_id;
  
  private String delivery_status;
  
  private LocalDateTime assigned_at;
  
  private LocalDateTime delivered_at;
  
  private String deliver_code;
  
  private String order_id;
  
  private String partner_id ;
  
}
