package com.matt.sbdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matt.sbdemo.data.ProductRepository;
import com.matt.sbdemo.deals.BundleDeal;
import com.matt.sbdemo.deals.Deal;
import com.matt.sbdemo.deals.DealManager;
import com.matt.sbdemo.deals.SingleProductDeal;
import com.matt.sbdemo.model.ApplyDealRequest;
import com.matt.sbdemo.model.ApplyDealResponse;
import com.matt.sbdemo.model.Product;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/v1/manage")
public class ManagerController {
	
	@Autowired
	private ProductRepository repo;
	@Autowired
	private DealManager dealManager;
	
	@PostMapping("createProduct")
	@Operation(description = "Create a new product - leave ID as null")
	public Product createProduct(@RequestBody Product product){
		return repo.save(product);
	}

	//Change price or description
	@Operation(description = "Edit Product - Change price or description.")
	@PostMapping("editProduct")
	public ResponseEntity<Product> editProduct(@RequestBody Product editedProduct){
		ResponseEntity<Product> response = null;
		
		//ID can't be null
		if(editedProduct.getId() == null) {
			response = ResponseEntity.badRequest().build();
		}
		//check if this product doesn't exist yet - can't edit it
		else if(!repo.existsById(editedProduct.getId())) {
			response = ResponseEntity.badRequest().build();
		}else {
			response = ResponseEntity.ok(repo.save(editedProduct));
		}
		
		return response;
	}
	
	@DeleteMapping("deleteProduct")
	@Operation(description = "Delete the product")
	public void removeProduct(@RequestBody Product product){
		repo.delete(product);
	}
	
	@GetMapping("availableDeals")
	@Operation(description = "Get all available deals")
	public Set<? extends SingleProductDeal> getAvailableDeals(){
		return dealManager.getAvailableDeals();
	}
	
	@GetMapping("availableBundleDeals")
	@Operation(description = "Get all available bundle deals")
	public Set<? extends BundleDeal> getAvailableBundleDeals(){
		return dealManager.getAvailableBundleDeals();
	}
	
	@PostMapping("applyDeal")
	@Operation(description = "Apply/activate a standalone or bundle deal - Supply the discountCode. If not a "
			+ "bundle deal, ignore id2 and set id1 for the product to which the deal applies.  Supply both"
			+ " ids for bundle deals.")
	public ApplyDealResponse applyDeal(@RequestBody ApplyDealRequest req){
		Deal deal = dealManager.activateDeal(req);
		Boolean result = deal == null ? false : true;
		return ApplyDealResponse.builder().dealApplied(result).build();
	}
	
	@DeleteMapping("clearActiveDeals")
	@Operation(description = "Clear/inactivate active deals")
	public void clearActiveDeals() {
	    dealManager.getActiveDeals().clear();
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
