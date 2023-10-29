package com.matt.sbdemo.deals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.matt.sbdemo.deals.Bogo50PercentDeal;
import com.matt.sbdemo.deals.BogoBundleDeal;
import com.matt.sbdemo.deals.BundleDeal;
import com.matt.sbdemo.deals.Deal;
import com.matt.sbdemo.deals.DealManager;
import com.matt.sbdemo.deals.SingleProductDeal;
import com.matt.sbdemo.model.ApplyDealRequest;

public class DealManagerTest {

	private DealManager manager;
	
	public DealManagerTest() {
		manager = new DealManager();
	}
	
	@BeforeEach
	public void refresh() {
		manager.getActiveDeals().clear();
		manager.getAvailableDeals().clear();
	}
	
	@Test
	public void testAvailableDeals() {
		SingleProductDeal deal = new Bogo50PercentDeal();
		manager.addDeal(deal);
		
		assertTrue(manager.getAvailableDeals().contains(deal),
				"Deals should be added.");
	}
	
	@Test
	public void testAvailableBundleDeals() {
		BundleDeal deal = new BogoBundleDeal();
		manager.addBundleDeal(deal);
		
		assertTrue(manager.getAvailableBundleDeals().contains(deal),
				"Deals should be added.");
	}
	@Test
	public void testActivateDeal() {
		manager.addDeal(new Bogo50PercentDeal());
		ApplyDealRequest request1 = ApplyDealRequest.builder().discountCode("BOGO50").id1(1L).build();
		Deal bogoDeal = manager.activateDeal(request1);
		List<Deal> active = manager.getActiveDeals();
		assertTrue(active.contains(bogoDeal), "It should have activated the 50% deal.");
		
		manager.addBundleDeal(new BogoBundleDeal());
		
		bogoDeal = manager.activateDeal(new ApplyDealRequest("BOGOBUNDLE", 3L, 2L));
		active = manager.getActiveDeals();
		assertTrue(active.contains(bogoDeal), "It should have activated the BOGO deal.");
	}
	
	@Test
	public void testActivateDealFailure() {
		manager.addDeal(new Bogo50PercentDeal());
		
		Deal bogoDeal = manager.activateDeal(new ApplyDealRequest("BOGO50000", 1L, null));//wrong discount code
		assertNull(bogoDeal, "Returns null if not activated");
		List<Deal> active = manager.getActiveDeals();
		assertFalse(active.size() > 0, "It should be empty.");
	}
}
