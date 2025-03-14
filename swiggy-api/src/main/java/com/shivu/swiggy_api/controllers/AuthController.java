package com.shivu.swiggy_api.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shivu.swiggy_api.entity.DeliveryPartner;
import com.shivu.swiggy_api.entity.Restaurant;
import com.shivu.swiggy_api.entity.User;
import com.shivu.swiggy_api.exception.DeliveryException;
import com.shivu.swiggy_api.exception.EmailException;
import com.shivu.swiggy_api.exception.RestaurantException;
import com.shivu.swiggy_api.exception.UserException;
import com.shivu.swiggy_api.request.CreateDeliveryPartnerRequest;
import com.shivu.swiggy_api.request.DeliveryPartnerSigninRequest;
import com.shivu.swiggy_api.request.ForgotPasswordRequest;
import com.shivu.swiggy_api.request.OTPRequest;
import com.shivu.swiggy_api.request.OTPVerify;
import com.shivu.swiggy_api.request.PasswordResetRequest;
import com.shivu.swiggy_api.request.RestauarantSignIn;
import com.shivu.swiggy_api.request.RestaurantSignUpRequest;
import com.shivu.swiggy_api.request.UserSignInRequest;
import com.shivu.swiggy_api.request.UserSignUpRequest;
import com.shivu.swiggy_api.services.IDeliveryPartnerService;
import com.shivu.swiggy_api.services.IRestaurantService;
import com.shivu.swiggy_api.services.IUserService;
import com.shivu.swiggy_api.utils.EmailUtils;
import com.shivu.swiggy_api.utils.EmailVerificationStore;
import com.shivu.swiggy_api.utils.JwtUtil;
import com.shivu.swiggy_api.utils.RandomGenerator;
import com.shivu.swiggy_api.utils.RequestEmail;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final IUserService userService;
	private final IRestaurantService restaurantService;
	private final EmailUtils emailUtils;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final IDeliveryPartnerService deliveryPartnerService;
	
	@Value("${FRONTEND_URL}")
	private String frontendUrl;

	public AuthController(IUserService userService, IRestaurantService restaurantService, EmailUtils emailUtils,
			PasswordEncoder passwordEncoder, JwtUtil jwtUtil, IDeliveryPartnerService deliveryPartnerService) {
		this.userService = userService;
		this.restaurantService = restaurantService;
		this.emailUtils = emailUtils;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.deliveryPartnerService = deliveryPartnerService;
	}

	@PostMapping("/user/signup")
	public ResponseEntity<?> userSignUpHandler(@Valid @RequestBody UserSignUpRequest request,
			BindingResult bindingResult) {

		// Checking for data
		if (bindingResult.hasErrors()) {
			throw new UserException("Invalid user Data");
		}

		// Check for email exists
		User userExists = userService.findByEmail(request.getEmail());
		if (userExists != null) {
			throw new UserException("Email Already Exists");
		}

		User user = new User();
		user.setAddress(request.getAddress());
		user.setCreatedAt(LocalDateTime.now());
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhoneNumber(request.getPhoneNumber());

		// save user
		User createdUser = userService.createUser(user);

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", createdUser.getEmail());
		claims.put("role", "customer");

		String generatedToken = jwtUtil.generateToken(createdUser.getEmail(), claims);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", generatedToken);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/user/signin")
	public Map<String, String> userSignInHandler(@Valid @RequestBody UserSignInRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new UserException("Invalid Data");
		}

		User findUser = userService.findByEmail(request.getEmail());
		if (findUser == null) {
			throw new UserException("Email Not Found");
		}
		if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword())) {
			throw new UserException("Incorrect password");
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "customer");
		claims.put("email", findUser.getEmail());

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", jwtUtil.generateToken(findUser.getEmail(), claims));

		return response;

	}

	@PostMapping("/restaurant/singup")
	public Map<String, String> restaurantSignUp(@Valid @RequestBody RestaurantSignUpRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new RestaurantException("Invalid Input data");
		}

		Restaurant restaurantExists = restaurantService.findByEmail(request.getEmail());
		if (restaurantExists != null) {
			throw new RestaurantException("Email Already Exists");
		}

		Restaurant restaurant = new Restaurant();
		restaurant.setAddress(request.getAddress());
		restaurant.setCreatedAt(LocalDateTime.now());
		restaurant.setEmail(request.getEmail());
		restaurant.setName(request.getName());
		restaurant.setPassword(passwordEncoder.encode(request.getPassword()));
		restaurant.setPhoneNumber(request.getPhoneNumber());
		restaurant.setRating(0.0);
		restaurant.setReviewsCount(0);

		Restaurant createdRestauarnt = restaurantService.createRestaurant(restaurant);

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "restaurant");
		claims.put("email", createdRestauarnt.getEmail());

		Map<String, String> response = new HashMap<>();
		response.put("success", "true");
		response.put("token", jwtUtil.generateToken(createdRestauarnt.getEmail(), claims));
		return response;

	}

	@PostMapping("/restaurant/singin")
	public Map<String, String> restaurantSignIn(@Valid @RequestBody RestauarantSignIn request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new UserException("Invalid Data");
		}
		Restaurant findRestaurant = restaurantService.findByEmail(request.getEmail());
		if (findRestaurant == null) {
			throw new RestaurantException("Email Not Found");
		}
		if (!passwordEncoder.matches(request.getPassword(), findRestaurant.getPassword())) {
			throw new RestaurantException("Incorrect password");
		}
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "restaurant");
		claims.put("email", findRestaurant.getEmail());
		Map<String, String> response = new HashMap<>();
		response.put("success", "true");
		response.put("token", jwtUtil.generateToken(findRestaurant.getEmail(), claims));
		return response;
	}

	@PostMapping("/delivery/signup")
	public ResponseEntity<?> createDeliveryPartner(@Valid @RequestBody CreateDeliveryPartnerRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new DeliveryException("Invalid input data");
		}
		if (deliveryPartnerService.getByEmail(request.getEmail()) != null) {
			throw new DeliveryException("Email Already exists");
		}

		DeliveryPartner deliveryPartner = new DeliveryPartner();
		deliveryPartner.setCreatedAt(LocalDateTime.now());
		deliveryPartner.setEmail(request.getEmail());
		deliveryPartner.setName(request.getName());
		deliveryPartner.setPassword(passwordEncoder.encode(request.getPassword()));
		deliveryPartner.setPhoneNumber(request.getPhoneNumber());
		deliveryPartner.setVehicleDetails(request.getVehicleNumber());

		deliveryPartner = deliveryPartnerService.create(deliveryPartner);

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "delivery");
		claims.put("email", deliveryPartner.getEmail());

		Map<String, Object> response = new HashMap<>();

		response.put("status", "success");
		response.put("token", jwtUtil.generateToken(deliveryPartner.getEmail(), claims));

		return ResponseEntity.ok(response);

	}

	@PostMapping("/delivery/signin")
	public ResponseEntity<?> deliveryPartnerSignIn(@RequestBody DeliveryPartnerSigninRequest request) {
		if (request.getEmail() == null || request.getPassword() == null) {
			throw new DeliveryException("Invalid input data");
		}

		DeliveryPartner findDeliveryPartner = deliveryPartnerService.getByEmail(request.getEmail());

		if (findDeliveryPartner == null) {
			throw new DeliveryException("Email not exists");
		}

		if (!passwordEncoder.matches(request.getPassword(), findDeliveryPartner.getPassword())) {
			throw new DeliveryException("Incorrect Pasword");
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "delivery");
		claims.put("email", findDeliveryPartner.getEmail());
		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", jwtUtil.generateToken(findDeliveryPartner.getEmail(), claims));
		response.put("userDetails", findDeliveryPartner);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPasswordHanlder(@RequestBody ForgotPasswordRequest request) {
		Map<String, Object> response = new HashMap<>();

		String role = request.getRole();
		String email = request.getEmail();
		LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(15);
		String resetToken = RandomGenerator.generateSecureToken();
		
		String username =null;

		if (role.equals("customer")) {
			User user = userService.findByEmail(email);
			if (user == null) {
				throw new UserException("Email Not Found");
			}
			user.setPasswordResetToken(resetToken);
			user.setPasswordExpiredBy(expiredTime);
			username = user.getName();
			userService.updateUser(user);
		} else if (role.equals("restaurant")) {
			Restaurant restaurant = restaurantService.findByEmail(email);
			if (email == null) {
				throw new RestaurantException("Email Not Found");
			}

			restaurant.setPasswordResetToken(resetToken);
			restaurant.setPasswordExpiredBy(expiredTime);
			username = restaurant.getName();
			restaurantService.upadetRestaurant(restaurant);
		} else if (role.equals("delivery")) {
			DeliveryPartner deliveryPartner = deliveryPartnerService.getByEmail(email);
			if (deliveryPartner == null) {
				throw new DeliveryException("Email Not Found");
			}

			deliveryPartner.setPasswordResetToken(resetToken);
			deliveryPartner.setPasswordExpiredBy(expiredTime);
			username = deliveryPartner.getName();
			deliveryPartnerService.update(deliveryPartner);
		} else {
			throw new UserException("Invalid User");
		}

		String subject = "Password Reset Request";
		String resetLink = frontendUrl+"auth/reset-password?token=" + resetToken + "&role="+role;

		String body = "Dear "+username+",\n\n"
				+ "We received a request to reset your password. Please click the link below to set a new password:\n\n"
				+ resetLink + "\n\n"
				+ "This link is valid for 10 minutes. If you did not request this, please ignore this email.\n\n"
				+ "Best regards,\n" + "Team Swiggy";

		try {
			emailUtils.sendEmail(request.getEmail(), subject, body);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new EmailException("Failed to send otp");
		}

		response.put("status", "success");
		response.put("message", "Password reset linksent successfully to " + email);

		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/resetpassword")
	public ResponseEntity<?> resetPasswordHanlder(
			@RequestBody PasswordResetRequest request)
	{
		Map<String, Object> response = new HashMap<>();
		
		String role = request.getRole();
		String token = request.getToken();
		
		if(role.equals("customer"))
		{
			User user  = userService.findByPasswordResetToken(token);
			
			if(user ==null)
			{
				throw new UserException("Invalid Token");
			}
			if(LocalDateTime.now().isAfter(user.getPasswordExpiredBy()))
			{
				user.setPasswordExpiredBy(null);
				user.setPasswordResetToken(null);
				userService.updateUser(user);
				throw new UserException("Token Expired");	
			}
			
			user.setPassword(passwordEncoder.encode(request.getNewPassword()));
			user.setPasswordResetToken(null);
			user.setPasswordExpiredBy(null);
			userService.updateUser(user);
			
		}else if(role.equals("restaurant"))
		{
			Restaurant restaurant =  restaurantService.findByPasswordResetToken(token);
			if(restaurant ==null)
			{
				throw new RestaurantException("Invalid token");
			}
			
			if(LocalDateTime.now().isAfter(restaurant.getPasswordExpiredBy()))
			{
				restaurant.setPasswordExpiredBy(null);
				restaurant.setPasswordResetToken(null);
				restaurantService.upadetRestaurant(restaurant);
				throw new RestaurantException("Token expired");
			}
			restaurant.setPassword(passwordEncoder.encode(request.getNewPassword()));
			restaurant.setPasswordExpiredBy(null);
			restaurant.setPasswordResetToken(null);
			restaurantService.upadetRestaurant(restaurant);			
		}else if(role.equals("delivery"))
		{
			DeliveryPartner deliveryPartner = deliveryPartnerService.findByPasswordResetToken(token);
			if(deliveryPartner ==null)
			{
				throw new DeliveryException("Invalid Token");
			}
			
			if(LocalDateTime.now().isAfter(deliveryPartner.getPasswordExpiredBy()))
			{
				deliveryPartner.setPasswordExpiredBy(null);
				deliveryPartner.setPasswordResetToken(null);
				deliveryPartnerService.update(deliveryPartner);
				throw new DeliveryException("Token expired");
			}
			deliveryPartner.setPassword(passwordEncoder.encode(request.getNewPassword()));
			deliveryPartner.setPasswordExpiredBy(null);
			deliveryPartner.setPasswordResetToken(null);
			deliveryPartnerService.update(deliveryPartner);
		}
		else {
			throw new UserException("Invalid User");
		}
		
		
		response.put("status","success");
		response.put("message","Password reset successfully");
		return ResponseEntity.ok(response);
		
	}
	
	

	@PostMapping("/otp")
	public Map<String, String> requestOTP(@Valid @RequestBody OTPRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new EmailException("Invalid Data");
		}

		if (request.getUserType().equalsIgnoreCase("customer")) {
			if (userService.findByEmail(request.getEmail()) != null) {
				throw new EmailException("Email Already Exists");
			}
		} else if (request.getUserType().equalsIgnoreCase("restaurant")) {
			if (restaurantService.findByEmail(request.getEmail()) != null) {
				throw new EmailException("Email Already Exists");
			}
		} else if (request.getUserType().equalsIgnoreCase("delivery")) {
			if (deliveryPartnerService.getByEmail(request.getEmail()) != null) {
				throw new DeliveryException("Email ALready Exists");
			}
		}
		String generatedOTP = RandomGenerator.generateNumberString(6);
		String subject = "Email Verification";
		String body = "Dear User,\n\n"
				+ "Thank you for signing up. Please use the OTP below to verify your email and complete the registration process.\n\n"
				+ "Your OTP: " + generatedOTP + "\n\n"
				+ "This OTP is valid for 10 minutes. Do not share it with anyone.\n\n"
				+ "If you did not request this, please ignore this email.\n\n" + "Best regards,\n" + "Team Swiggy";

		String htmlBody = "<html>"
				+ "<body style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>"
				+ "<div style='max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1);'>"
				+ "<h2 style='color: #333; text-align: center;'>Email Verification</h2>"
				+ "<p style='font-size: 16px; color: #555;'>Dear User,</p>"
				+ "<p style='font-size: 16px; color: #555;'>Thank you for signing up. Please use the OTP below to verify your email and complete the registration process.</p>"
				+ "<div style='text-align: center; margin: 20px 0;'>"
				+ "<span style='font-size: 24px; font-weight: bold; color: #4CAF50; padding: 10px 20px; border-radius: 5px; background: #eaf7ea; display: inline-block;'>"
				+ generatedOTP + "</span>" + "</div>"
				+ "<p style='font-size: 16px; color: #555;'>This OTP is valid for <strong>10 minutes</strong>. Do not share it with anyone.</p>"
				+ "<p style='font-size: 16px; color: #555;'>If you did not request this, please ignore this email.</p>"
				+ "<br>" + "<p style='font-size: 16px; color: #555;'>Best regards,<br><strong>Team Swiggy</strong></p>"
				+ "</div>" + "</body>" + "</html>";

		try {
			emailUtils.sendEmail(request.getEmail(), subject, body);
			RequestEmail requestEmail = new RequestEmail();
			requestEmail.setEmail(request.getEmail());
			requestEmail.setOtp(generatedOTP);
			EmailVerificationStore.addEmail(requestEmail);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new EmailException("Failed to send otp");
		}

		Map<String, String> apiResponse = new HashMap<>();
		apiResponse.put("success", "true");
		apiResponse.put("email", request.getEmail());
		return apiResponse;
	}

	@PostMapping("/otp/verify")
	public Map<String, String> verifyOTP(@Valid @RequestBody OTPVerify request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new EmailException("Invalid Data");
		}

		RequestEmail requestEmail = EmailVerificationStore.getOtp(request.getEmail());
		if (requestEmail == null) {
			throw new EmailException("Invalid Email");
		}

		if (!requestEmail.getOtp().equals(request.getOtp())) {
			throw new EmailException("Incorrect OTP");
		}

		EmailVerificationStore.deleteEmailData(request.getEmail());

		Map<String, String> apiResponse = new HashMap<>();
		apiResponse.put("success", "true");
		apiResponse.put("message", "OTP verified Successfully");
		return apiResponse;

	}

}
