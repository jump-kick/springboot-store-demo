package com.matt.sbdemo.controller;

import static com.matt.sbdemo.util.TestUtils.getProductA;
import static com.matt.sbdemo.util.TestUtils.getProductB;
import static com.matt.sbdemo.util.TestUtils.getProductC;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.sbdemo.data.ProductRepository;
import com.matt.sbdemo.model.Basket;
import com.matt.sbdemo.model.Product;
import com.matt.sbdemo.model.ProductRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopperControllerTest {

	@Autowired
	private ProductRepository repo;
	@Autowired
	private MockMvc mvc;
	@Autowired
	private Basket basket;

	private Product prod = null;
	
	private static final String V1 = "/v1";
	private static final String SHOP = "/shop";
	
	@BeforeEach
	public void setup() {
		basket.empty();
		repo.save(getProductA());
		repo.save(getProductB());
		repo.save(getProductC());
		prod = repo.findAll().iterator().next();
	}
	
	@Test
	public void testAddProduct() throws Exception {
		String p = new ObjectMapper().writeValueAsString(ProductRequest.builder().id(prod.getId()).quantity(1).build());
		mvc.perform(post(V1 + SHOP + "/addToBasket").contentType(MediaType.APPLICATION_JSON).content(p))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(getProductA().getDescription())))
			.andDo(print());
		
		Integer count = basket.getBasket().get(prod);
	    assertEquals(1, count, "Should exist after adding");
		
	}
	
	@Test
	public void testAddMultipleProduct() throws Exception {
		String p = new ObjectMapper().writeValueAsString(ProductRequest.builder()
				.id(prod.getId())
				.quantity(3)
				.build());
		mvc.perform(post(V1 + SHOP + "/addToBasket").contentType(MediaType.APPLICATION_JSON).content(p))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(getProductA().getDescription())))
			.andDo(print());
		
		Integer count = basket.getBasket().get(prod);
	    assertEquals(3, count, "Should exist after adding");
		
	}
	
	@Test
	public void testUpdateQuantity() throws Exception {
		String p = new ObjectMapper().writeValueAsString(ProductRequest.builder().id(prod.getId()).quantity(1).build());
		mvc.perform(post(V1 + SHOP + "/addToBasket").contentType(MediaType.APPLICATION_JSON).content(p));
		
		p = new ObjectMapper().writeValueAsString(ProductRequest.builder().id(prod.getId()).quantity(3).build());
		mvc.perform(put(V1 + SHOP + "/updateQuantity").contentType(MediaType.APPLICATION_JSON).content(p))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(getProductA().getDescription())))
		.andDo(print());
		
		Integer count = basket.getBasket().get(prod);
		
	    assertEquals(3, count, "Should be updated to 3");
		
	}
	
	@Test
	public void testRemoveAllOfAProduct() throws Exception {
		String p = new ObjectMapper().writeValueAsString(ProductRequest.builder().id(prod.getId()).quantity(1).build());
		mvc.perform(post(V1 + SHOP + "/addToBasket").contentType(MediaType.APPLICATION_JSON).content(p));
		
		p = new ObjectMapper().writeValueAsString(ProductRequest.builder().id(prod.getId()).quantity(3).build());
		mvc.perform(put(V1 + SHOP + "/updateQuantity").contentType(MediaType.APPLICATION_JSON).content(p));
		
		Integer count = basket.getBasket().get(prod);
		
	    assertEquals(3, count, "Should be updated to 3");
		
		mvc.perform(delete(V1 + SHOP + "/removeAll/" + prod.getId()).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print());
		
		assertNull(basket.getBasket().get(prod),
				"Product A shouldn't be in the basket anymore.");
	}
	
	@Test
	public void testGetTotal() throws Exception {

	}
}
