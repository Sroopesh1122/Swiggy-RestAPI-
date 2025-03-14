package com.shivu.swiggy_api.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest 
{
  private String email;
  private String role;
}
