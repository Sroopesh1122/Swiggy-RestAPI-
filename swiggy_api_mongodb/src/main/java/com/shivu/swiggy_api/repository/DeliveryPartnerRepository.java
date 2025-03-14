package com.shivu.swiggy_api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shivu.swiggy_api.entity.DeliveryPartner;

@Repository
public interface DeliveryPartnerRepository extends MongoRepository<DeliveryPartner, String> {

	public Optional<DeliveryPartner> findByEmail(String email);

	
	public Optional<DeliveryPartner> findByPasswordResetToken(String passwordResetToken);
}
