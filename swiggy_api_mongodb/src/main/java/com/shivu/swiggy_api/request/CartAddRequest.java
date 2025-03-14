package com.shivu.swiggy_api.request;

import lombok.Data;

@Data
public class CartAddRequest
{
  String menuId;
  String userId;
}
