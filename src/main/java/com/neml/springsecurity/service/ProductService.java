package com.neml.springsecurity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neml.springsecurity.model.ProductModel;
import com.neml.springsecurity.model.UserInfo;
import com.neml.springsecurity.repository.UserInfoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {

	@Autowired
	private UserInfoRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	List<ProductModel> list=null;
	
	@PostConstruct
	    public void init() {
	        list = new ArrayList<>();
	        for (int i = 1; i <= 100; i++) {
	            ProductModel product = new ProductModel();
	            product.setId(new Random().nextInt(1000));
	            product.setName("Product " + i);
	            product.setQty(new Random().nextInt(10));
	            list.add(product);
	        }
	    }
	
	public List<ProductModel> getProducts() {
		
		return list;
	}

	public ProductModel getProduct(int id) {
		
		return list.stream()
				.filter(product -> product.getId()==id)
				.findAny()
				.orElseThrow(()->new RuntimeException("product "+id+" not found"));
	}

	public String addUser(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		System.out.println(userInfo.getPassword());
		repo.save(userInfo);
		return "User added to the system";
	}
}
