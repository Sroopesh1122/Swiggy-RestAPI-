package com.shivu.swiggy_api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestauarantSignIn 
{
 @NotBlank(message = "Required")	
 private String email;
 
 @NotBlank(message = "Required")
 private String password;
}
