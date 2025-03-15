package com.shivu.swiggy_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivu.swiggy_api.entity.DeliveryPartner;
import com.shivu.swiggy_api.services.DeliveryDetails;

@RestController
@RequestMapping("/delivery")
public class DevileryPartnerController
{

   @GetMapping("/secure/profile")
   @PreAuthorize("hasRole('DELIVERY')")
   public ResponseEntity<?> getProfile(
		   @AuthenticationPrincipal DeliveryDetails deliveryDetails)
   { 
	    
		DeliveryPartner deliveryPartner = deliveryDetails.getUser();
		deliveryPartner.setPassword(null);
		return ResponseEntity.ok(deliveryPartner);
		
   }
}
