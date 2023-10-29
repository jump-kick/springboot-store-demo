package com.matt.sbdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matt.sbdemo.data.ProductRepository;
import com.matt.sbdemo.model.Basket;
import com.matt.sbdemo.model.Product;
import com.matt.sbdemo.model.ProductRequest;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "shop")
public class ShopperController {
	
	@Autowired
	private Basket basket;
	@Autowired
	private ProductRepository repo;
	
	@Operation(description = "Add To Basket - if quantity is left blank, default is 1.")
	@PostMapping("addToBasket")
	public Map<Product, Integer> addToBasket(@RequestBody ProductRequest req){
		int quant = req.getQuantity() == null ? 1 : req.getQuantity();
		basket.add(repo.findById(req.getId()).get(), quant);
		return basket.getBasket();
	}
	
	@Operation(description = "Update the product quantity")
	@PutMapping("updateQuantity")
	public Map<Product, Integer> updateBasket(@RequestBody ProductRequest req){
		basket.updateProduct(repo.findById(req.getId()).get(), req.getQuantity());
		return basket.getBasket();
	}

	@Operation(description = "Remove a product")
	@DeleteMapping("removeAll/{id}")
	public void removeAll(@PathVariable Long id){
		Product p = repo.findById(id).get();
		
		if(p == null) {
			return;
		}
		
		basket.removeAll(p);
	}
	
	@Operation(description = "Get the total, while applying any active deals")
	@GetMapping("basketTotal")
	public Double getTotal() {
		return basket.getTotal();
	}
	
	@Operation(description = "Get all available products")
	@GetMapping("getProducts")
	public List<Product> getProducts(){
		ArrayList<Product> list = new ArrayList<>();
		for(Product p: repo.findAll()) {
			list.add(p);
		}
		
		return list;
	}
}
