package com.matt.sbdemo.model;

import static com.matt.sbdemo.util.TestUtils.getProductA;
import static com.matt.sbdemo.util.TestUtils.getProductB;
import static com.matt.sbdemo.util.TestUtils.getProductC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.matt.sbdemo.deals.DealManager;

@ExtendWith(SpringExtension.class)
public class BasketTest {
	
	@Autowired
	private Basket basket;
	
	@TestConfiguration
	public static class BasketTestConfig{
		@Bean
		public Basket basket() {
			return new Basket();
		}
		
		@Bean
		public DealManager dealManager() {
			return new DealManager();
		}
	}
	
	@BeforeEach
	public void emptyBasket() {
		basket.empty();
	}
	
	@Test
	public void testAddOneProduct() {
		basket.add(getProductA(), 1);
		assertEquals(1, basket.getBasket().get(getProductA()), 
				"There should be one in the basket.");
	}

	@Test
	public void testAddTwoProducts() {
		basket.add(getProductA(), 2);
		assertEquals(2, basket.getBasket().get(getProductA()), 
				"There should be two in the basket.");
	}
	
	@Test
	public void testAddTwoDifferentProducts() {
		basket.add(getProductA(), 1);
		basket.add(getProductB(), 1);
		assertEquals(1, basket.getBasket().get(getProductA()), 
				"There should be one product A in the basket.");
		assertEquals(1, basket.getBasket().get(getProductB()), 
				"There should be one product B in the basket.");
	}
	
	@Test
	public void testAddManyDifferentProducts() {
		basket.add(getProductA(), 1);
		basket.add(getProductB(), 1);
		basket.add(getProductA(), 1);
		basket.add(getProductB(), 1);
		basket.add(getProductC(), 1);
		basket.add(getProductC(), 1);
		basket.add(getProductC(), 1);
		basket.add(getProductC(), 1);
		assertEquals(2, basket.getBasket().get(getProductA()), 
				"There should be two product As in the basket.");
		assertEquals(2, basket.getBasket().get(getProductB()), 
				"There should be two product Bs in the basket.");
		assertEquals(4, basket.getBasket().get(getProductC()), 
				"There should be four product Cs in the basket.");
	}
	
	@Test
	public void testUpdateProduct(){
		testAddManyDifferentProducts();//load the basket
		basket.updateProduct(getProductA(), 3);
		assertEquals(3, basket.getBasket().get(getProductA()), 
				"There should be three product As in the basket.");

		basket.updateProduct(getProductC(), 1);
		assertEquals(1, basket.getBasket().get(getProductC()), 
				"There should be 1 product Cs in the basket.");
		
	}
	
	@Test
	public void testRemoveOneProduct(){
		testAddManyDifferentProducts();//load the basket
		basket.removeOne(getProductA());
		assertEquals(1, basket.getBasket().get(getProductA()), 
				"There should be one product A in the basket.");
		basket.removeOne(getProductA());
		assertNull(basket.getBasket().get(getProductA()), 
				"There should be no product A in the basket.");
		basket.removeOne(getProductC());
		assertEquals(3, basket.getBasket().get(getProductC()), 
				"There should be three product Cs in the basket.");
		assertEquals(2, basket.getBasket().get(getProductB()), 
				"There should be two product Bs in the basket.");		
	}
	
	@Test
	public void testRemoveAllProducts(){
		testAddManyDifferentProducts();//load the basket
		basket.removeAll(getProductC());
		assertEquals(2, basket.getBasket().get(getProductA()), 
				"There should be two product As in the basket.");
		assertEquals(2, basket.getBasket().get(getProductB()), 
				"There should be two product Bs in the basket.");
		assertNull(basket.getBasket().get(getProductC()), 
				"There should be no product Cs in the basket.");
	}
	
	@Test
	public void testGetTotal() {
		testAddManyDifferentProducts();//load the basket
		assertEquals(648.00d, basket.getTotal(), "Total should be $648.00");
		basket.add(getProductA(), 1);
		assertEquals(663.00d, basket.getTotal(), "Total should be $663.00");
		basket.removeAll(getProductC());
		assertEquals(63.00d, basket.getTotal(), "Total should be $63.00");
	}
}
