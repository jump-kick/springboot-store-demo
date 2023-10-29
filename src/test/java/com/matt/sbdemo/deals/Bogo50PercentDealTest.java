package com.matt.sbdemo.deals;

import static com.matt.sbdemo.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.matt.sbdemo.model.Basket;
import com.matt.sbdemo.model.Product;

@ExtendWith(SpringExtension.class)
public class Bogo50PercentDealTest {

	@Autowired
	private Basket basket;
	private Bogo50PercentDeal deal;
	
	public Bogo50PercentDealTest() {
		deal = new Bogo50PercentDeal(2L);
	}
	
	@TestConfiguration
	public static class DealTestConfig{
		
		@Bean
		public Basket basket() {
			return new Basket();
		}
		
		@Bean
		public DealManager dealManager() {
			return new DealManager();
		}
	}
	
	@Test
	public void testApplyDeal() {
		
		basket.add(getProductA_FixedId(), 1);
		basket.add(getProductB_FixedId(), 1);
		basket.add(getProductC_FixedId(), 1);
		Double total = basket.getTotal();
		
		assertEquals(174.00d, deal.applyDeal(basket.getBasket(), total),
				"There should be no affect on price yet.");
		
		basket.add(getProductB_FixedId(), 1);
		total = basket.getTotal();
		
		assertEquals(178.50d, deal.applyDeal(basket.getBasket(), total),
				"There should be a 50% discount on the 2nd product B.");
		
		basket.add(getProductB_FixedId(), 1);
		total = basket.getTotal();
		
		assertEquals(187.50d, deal.applyDeal(basket.getBasket(), total),
				"There should be a 50% discount on the 2nd productB, not the 3rd one.");
		
		basket.add(getProductB_FixedId(), 1);
		total = basket.getTotal();
		
		assertEquals(192.00d, deal.applyDeal(basket.getBasket(), total),
				"There should be two 50% discounts applied since there are 4 product Bs.");
	}
}
