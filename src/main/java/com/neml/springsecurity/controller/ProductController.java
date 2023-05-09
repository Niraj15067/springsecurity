package com.neml.springsecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neml.springsecurity.model.AuthRequest;
import com.neml.springsecurity.model.ProductModel;
import com.neml.springsecurity.model.UserInfo;
import com.neml.springsecurity.service.JWTService;
import com.neml.springsecurity.service.ProductService;

@RestController
@RequestMapping("/products")
@EnableMethodSecurity
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired 
	JWTService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "This endpoint is not secure";
	}

	@PostMapping("/new")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return productService.addUser(userInfo);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<ProductModel> getAllPrducts() {
		return productService.getProducts();
	}

	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")

	public ProductModel getProductById(@PathVariable int id) {
		return productService.getProduct(id);
	}
	
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
	org.springframework.security.core.Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	if(authentication.isAuthenticated())	
			return jwtService.generateToken(authRequest.getUsername());
	else
		throw new UsernameNotFoundException("Invalid userrequest!");
	}
}
