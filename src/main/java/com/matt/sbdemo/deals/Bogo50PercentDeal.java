package com.matt.sbdemo.deals;

import java.util.Map;
import java.util.Map.Entry;

import com.matt.sbdemo.model.Product;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Bogo50PercentDeal extends SingleProductDeal {

	public Bogo50PercentDeal() {
		super(null);
	}
	//Supply the id of the Product for which this applies
	public Bogo50PercentDeal(Long id) {
		super(id);
	}
	
	@Override
	public Double applyDeal(Map<Product, Integer> pMap, Double total) {
		
		Double discount = 0.0;
		for(Entry<Product, Integer> e: pMap.entrySet()) {
			if(e.getKey().getId().equals(this.getId())) {
				//dividing by int discards any remainder for an odd number
				//quantity - only get a discount for every 2 items
				int numDiscounts = e.getValue() / 2;
				discount = (e.getKey().getPrice() / 2) * numDiscounts;
				break;
			}
		}
		
		return total - discount;
	}

	@Override
	public String getDiscountCode() {
		return "BOGO50";
	}

	@Override
	public String getDescription() {
		
		return "Buy One, Get One 50% off!";
	}

}
