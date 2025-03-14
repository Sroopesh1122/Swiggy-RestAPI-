package com.shivu.swiggy_api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.shivu.swiggy_api.entity.User;
import java.util.List;




@Repository
public interface UserRepository extends MongoRepository<User, String> 
{
   public Optional<User> findByEmail(String email);
   
   public Optional<User> findByPasswordResetToken(String passwordResetToken);
}
