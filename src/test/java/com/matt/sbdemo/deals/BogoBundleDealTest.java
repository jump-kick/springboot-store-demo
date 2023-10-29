package com.matt.sbdemo.deals;

import static com.matt.sbdemo.util.TestUtils.getProductA_FixedId;
import static com.matt.sbdemo.util.TestUtils.getProductB_FixedId;
import static com.matt.sbdemo.util.TestUtils.getProductC_FixedId;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.matt.sbdemo.model.ApplyDealRequest;
import com.matt.sbdemo.model.Basket;

@ExtendWith(SpringExtension.class)
public class BogoBundleDealTest {

	@Autowired
	private Basket basket;
	private static Deal deal;
	
	public BogoBundleDealTest(){
		//Buy product C, get product B free
		deal = new BogoBundleDeal(3L, 2L);
	}
	
	@TestConfiguration
	public static class BogoBundleTestConfig{
		@Bean
		public Basket basket() {
			return new Basket();
		}
		@Bean
		public DealManager dealManager() {
			DealManager dm = new DealManager();
			dm.addBundleDeal((BundleDeal)deal);
			dm.activateDeal(new ApplyDealRequest("BOGOBUNDLE", 3L, 2L));
			return dm;
		}
	}
	@Test
	public void testApplyDeal() {
		basket.add(getProductA_FixedId(), 1);
		basket.add(getProductB_FixedId(), 1);

		Double total = basket.getTotal();
		
		assertEquals(24.00d, deal.applyDeal(basket.getBasket(), total),
				"There should be no affect on price yet.");
		
		basket.add(getProductC_FixedId(), 1);
		total = basket.getTotal();
		
		assertEquals(165.00d, total,
				"There should be no charge for product B; 150 + 15.");
		
		basket.add(getProductB_FixedId(), 1);
		total = basket.getTotal();
		
		assertEquals(174.00d, total,
				"No deal for a 2nd product B; 150 + 15 + 9");
		
		basket.add(getProductC_FixedId(), 1);
		total = basket.getTotal();
		
		assertEquals(315.00d, total,
				"Total should be 150 + 150 + 15, got 2 product Bs free.");

	}
}
