package com.matt.sbdemo.deals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.matt.sbdemo.model.ApplyDealRequest;

public class DealManager {

	private Set<SingleProductDeal> availableDeals;
	private Set<BundleDeal> availableBundleDeals;
	private List<Deal> activeDeals;
	
	public DealManager() {
		availableDeals = new HashSet<>();
		availableBundleDeals = new HashSet<>();
		activeDeals = new ArrayList<>();
	}
	
	public boolean addDeal(SingleProductDeal deal){
		return availableDeals.add(deal);
	}
	
	public boolean addBundleDeal(BundleDeal deal){
		return availableBundleDeals.add(deal);
	}

	public Set<SingleProductDeal> getAvailableDeals() {
		
		return availableDeals;
	}

	public Set<BundleDeal> getAvailableBundleDeals() {
		
		return availableBundleDeals;
	}
	
	/**
	 * The manager can activate a deal for a product, adding it to the list of
	 * active deals.
	 * @param discountCode, or "coupon code" assigned for a deal
	 * @param id1 of the product to which to the deal applies
	 * @param id2 of the product to which to the bundle deal applies; supply null if not a
	 * bundle deal
	 * @return the Deal object, or null if the discountCode doesn't match a deal
	 */
	public Deal activateDeal(ApplyDealRequest dealRequest) {
		
		Deal deal = null;
		
		Set<Deal> allDeals = new HashSet<>();
		allDeals.addAll(availableDeals);
		allDeals.addAll(availableBundleDeals);
		
		for(Deal d: allDeals) {
			if(d.getDiscountCode().contentEquals(dealRequest.getDiscountCode())) {
				
				Constructor<? extends Deal> c;
				
				if(d instanceof SingleProductDeal) {
					
					try {
						c = d.getClass().getConstructor(Long.class);
						deal = c.newInstance(dealRequest.getId1());
					} catch (NoSuchMethodException | SecurityException | InstantiationException |
							IllegalAccessException | IllegalArgumentException | 
							InvocationTargetException e) {
						e.printStackTrace();
					}
				}else {//It's a bundle deal
					try {
						c = d.getClass().getConstructor(Long.class, Long.class);
						deal = c.newInstance(dealRequest.getId1(), dealRequest.getId2());
					} catch (NoSuchMethodException | SecurityException | InstantiationException |
							IllegalAccessException | IllegalArgumentException | 
							InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				
				break;
			}
		}
		
		if(deal == null) {
			return deal;
		}else if(activeDeals.add(deal)) {
			return deal;
		}else {
			return null;
		}
	}

	/**
	 * Returns the list of activated deal objects
	 * @return List of active deals
	 */
	public List<Deal> getActiveDeals() {
		return activeDeals;
	}

}
