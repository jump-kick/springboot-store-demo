package com.matt.sbdemo.deals;

import java.util.Map;

import com.matt.sbdemo.model.Product;

public interface Deal {
	
	abstract Double applyDeal(Map<Product, Integer> pMap, Double total);
	
	abstract String getDiscountCode();
	
	abstract String getDescription();

}
