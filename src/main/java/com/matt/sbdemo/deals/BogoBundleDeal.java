package com.matt.sbdemo.deals;

import java.util.Map;
import java.util.Map.Entry;

import com.matt.sbdemo.model.Product;

import lombok.EqualsAndHashCode;

/**\
 * If you buy a product with id1, you get the product with id2 for free.
 * @author matthew bankert
 *
 */

@EqualsAndHashCode(callSuper = false)
public class BogoBundleDeal extends BundleDeal {
	
	public BogoBundleDeal() {
		super(null, null);
	}
	
	public BogoBundleDeal(Long id1, Long id2) {
       super(id1, id2);
	}
	
	@Override
	public Double applyDeal(Map<Product, Integer> pMap, Double total) {
		int id1Count = 0;
		int id2Count = 0;
		double p2Price = 0.0d;
		double discount = 0.0d;
		
		for(Entry<Product, Integer> e: pMap.entrySet()) {
			if(e.getKey().getId() == getID1()) {
				id1Count = e.getValue();
			}else if(e.getKey().getId() == getID2()) {
				id2Count = e.getValue();
				p2Price = e.getKey().getPrice();
			}
			
			if(id1Count > 0 && id2Count > 0) {
				break;
			}
		}
		
		discount = (double)Math.min(id1Count, id2Count) * p2Price;
				
		return total - discount;
	}

	@Override
	public String getDiscountCode() {
		return "BOGOBUNDLE";
	}

	@Override
	public String getDescription() {
		return "Buy one product, get a different one free!";
	}

	
}
