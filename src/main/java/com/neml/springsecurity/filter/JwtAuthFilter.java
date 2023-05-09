package com.neml.springsecurity.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.neml.springsecurity.service.JWTService;
import com.neml.springsecurity.service.UserInfoUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UserInfoUserDetailsService userDetailsService;
	
	@Override
	//to check if the token is valid or not by extracting the token 
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		// Below is the entire token or string from which we need to extract the Bearer
		// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuaXJhajE1MDYiLCJpYXQiOjE2ODMyNzA5OTAsImV4cCI6MTY4MzI3Mjc5MH0.TWoeXbgI_Sp0ieMBvWAhkj-22UESte_j0Hc4-fiTfcY
		if (authHeader != null && authHeader.startsWith("Bearer")) {
			token = authHeader.substring(7);//step 1 : extract the token.
			username=jwtService.extractUsername(token);// step 2: extract the username
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			// based on the username it will load the username from the db and check if its macthing with the token
			//username if yes then authentication or authorization will be provided else not.
			UserDetails userDetails= userDetailsService.loadUserByUsername(username);
			
			if(jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
			filterChain.doFilter(request, response);
	}

}
