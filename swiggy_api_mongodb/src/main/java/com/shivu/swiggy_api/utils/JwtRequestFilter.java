package com.shivu.swiggy_api.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.shivu.swiggy_api.services.CustomerUserDetailsService;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final CustomerUserDetailsService userDetailsService;

	public JwtRequestFilter(JwtUtil jwtUtil, CustomerUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
        
		final String authorizationHeader = request.getHeader("Authorization");
		
		if (authorizationHeader != null) {

			String email = null;
			String jwt = null;
			String role = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractSubject(jwt);
				role = jwtUtil.extractClaims(jwt).get("role").toString();
			}

			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = null;

				if (role.equals("restaurant")) {
					userDetails = userDetailsService.loadRestaurantUser(email);
				}
				else if(role.equals("customer"))
				{
					userDetails = userDetailsService.loadCustomer(email);
				}
				
				else if(role.equals("delivery"))
				{
					userDetails = userDetailsService.loadDeliveryPartner(email);
					System.out.println("Founded");
				}
				
				
				Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

				var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
						userDetails, null, authorities);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} 
		}

		chain.doFilter(request, response);
	}
}
