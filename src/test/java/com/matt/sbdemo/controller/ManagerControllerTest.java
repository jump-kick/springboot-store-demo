package com.matt.sbdemo.controller;

import static com.matt.sbdemo.util.TestUtils.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.sbdemo.data.ProductRepository;
import com.matt.sbdemo.deals.Bogo50PercentDeal;
import com.matt.sbdemo.deals.BogoBundleDeal;
import com.matt.sbdemo.deals.DealManager;
import com.matt.sbdemo.model.ApplyDealRequest;
import com.matt.sbdemo.model.Product;

@SpringBootTest
@AutoConfigureMockMvc
public class ManagerControllerTest {
	
	@Autowired
	private ProductRepository repo;
	@Autowired
	private DealManager dealManager;
	@Autowired
	private MockMvc mvc;
	
	private static final String V1 = "/v1";
	private static final String MANAGE = "/manage";
	
	@BeforeEach
	public void deleteAll() {
		repo.deleteAll();
		dealManager.getActiveDeals().clear();
	}
	
	@Test
	public void testAddProduct() throws Exception {
		
		String p = getProductJson();
		mvc.perform(post(V1 + MANAGE + "/createProduct").contentType(MediaType.APPLICATION_JSON).content(p))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(getProductA().getDescription())))
			.andDo(print());
		
		Product prod = repo.findAll().iterator().next();
	    assertTrue(p.contains(prod.getDescription()), "Should exist after inserting");
		
	}

	@Test
	public void testEditDescrition() throws JsonProcessingException, Exception {

		Product prod = repo.save(getProductA());
		String desc = "New description!!";
		
		Product editedProd = new Product(prod.getId(), prod.getName(), prod.getPrice(), desc);
		mvc.perform(post(V1 + MANAGE + "/editProduct").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(editedProd)))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(desc)))
			.andDo(print());
		
	}

	@Test
	public void testEditPrice() throws JsonProcessingException, Exception {
		double newPrice = 2.50;
		Product prod = repo.save(getProductA());
		
		Product editedProd = new Product(prod.getId(), prod.getName(), newPrice, prod.getDescription());
		mvc.perform(post(V1 + MANAGE + "/editProduct").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(editedProd)))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(Double.toString(newPrice))))
			.andDo(print());
	}
	
	@Test
	public void testEditEndpointWithNewProduct() throws JsonProcessingException, Exception {

		Product saved = repo.save(getProductA());
		
		Product newProd = Product.builder().id(saved.getId() + 1).name("New")
				.description("It's new!").price(10.00).build();
		mvc.perform(post(V1 + MANAGE + "/editProduct").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newProd)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@Test
	public void testEditEndpointWithNullId() throws JsonProcessingException, Exception {

		Product newProd = Product.builder().name("New")
				.description("It's new!").price(10.00).build();
		mvc.perform(post(V1 + MANAGE + "/editProduct").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newProd)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@Test
	public void testDeleteProduct() throws JsonProcessingException, Exception {
		Product prod = repo.save(getProductA());
		
		mvc.perform(delete(V1 + MANAGE + "/deleteProduct").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(prod)))
			.andExpect(status().isOk())
			.andDo(print());
		
		assertFalse(repo.existsById(prod.getId()), "It shouldn't be there");
	}
	
	@Test
	public void testGetAvailableDeals() throws Exception {
		mvc.perform(get(V1 + MANAGE + "/availableDeals").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Buy One, Get One 50% off!")))
			//.andExpect(content().string(containsString("Buy one product, get a different one free!")))
			.andDo(print());
	}
	
	@Test
	public void testGetAvailableBundleDeals() throws Exception {
		mvc.perform(get(V1 + MANAGE + "/availableBundleDeals").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Buy one product, get a different one free!")))
			.andDo(print());
	}
	
	@Test
	public void testApplyDeal() throws JsonProcessingException, Exception {

		Bogo50PercentDeal deal = new Bogo50PercentDeal(1L);
		assertFalse(dealManager.getActiveDeals().contains(deal), "The deal should not be there.");
		
		mvc.perform(post(V1 + MANAGE + "/applyDeal").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(getBogo50Request())))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("true")))
			.andDo(print());
		assertTrue(dealManager.getActiveDeals().contains(deal), "The deal should be there now.");
	}
	
	@Test
	public void testApplyDealFail() throws JsonProcessingException, Exception {

		Bogo50PercentDeal deal = new Bogo50PercentDeal(1L);
		assertFalse(dealManager.getActiveDeals().contains(deal), "The deal should not be there.");
		ApplyDealRequest r = getBogo50Request();
		r.setDiscountCode("BOGOBOGOBOGO");
		mvc.perform(post(V1 + MANAGE + "/applyDeal").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(r)))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("false")))
			.andDo(print());
		assertFalse(dealManager.getActiveDeals().contains(deal), "The deal shouldn't be there.");
	}
	
	@Test
	public void testApplyBundleDeal() throws JsonProcessingException, Exception {

		BogoBundleDeal deal = new BogoBundleDeal(1L, 2L);
		assertFalse(dealManager.getActiveDeals().contains(deal), "The deal should not be there.");
		
		mvc.perform(post(V1 + MANAGE + "/applyDeal").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(getBogoBundleRequest())))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("true")))
			.andDo(print());
		assertTrue(dealManager.getActiveDeals().contains(deal), "The deal should be there now.");
	}
	
	@Test
	public void testApplyBundleDealFail() throws JsonProcessingException, Exception {

		Bogo50PercentDeal deal = new Bogo50PercentDeal(1L);
		assertFalse(dealManager.getActiveDeals().contains(deal), "The deal should not be there.");
		ApplyDealRequest r = getBogoBundleRequest();
		r.setDiscountCode("BOGOBOGOBOGO");
		mvc.perform(post(V1 + MANAGE + "/applyDeal").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(r)))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("false")))
			.andDo(print());
		assertFalse(dealManager.getActiveDeals().contains(deal), "The deal shouldn't be there.");
	}
	
	@Test
	public void testClearActiveDeals() throws JsonProcessingException, Exception {

		BogoBundleDeal deal = new BogoBundleDeal(1L, 2L);
		Bogo50PercentDeal deal1 = new Bogo50PercentDeal(1L);
		dealManager.activateDeal(new ApplyDealRequest(deal.getDiscountCode(), deal.getID1(), deal.getID2()));
		dealManager.activateDeal(new ApplyDealRequest(deal1.getDiscountCode(), deal1.getId(), null));
		assertTrue(dealManager.getActiveDeals().contains(deal), "The deal should be there now.");
		assertTrue(dealManager.getActiveDeals().contains(deal1), "The deal should be there now.");
		
		mvc.perform(delete(V1 + MANAGE + "/clearActiveDeals").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(getBogoBundleRequest())))
			.andExpect(status().isOk())
			.andDo(print());
		assertTrue(dealManager.getActiveDeals().isEmpty(), "Active deals should be cleared.");
	}
}
