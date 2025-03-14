package com.shivu.swiggy_api.request;

import lombok.Data;

@Data
public class PasswordResetRequest 
{
  private String token;
  private String role;
  private String newPassword;
}
