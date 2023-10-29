package com.matt.sbdemo.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.matt.sbdemo.backend.ManagerService;
import com.matt.sbdemo.data.ProductRepository;
import com.matt.sbdemo.model.Product;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ManagerServiceTest {
	
	@Autowired
	private ManagerService service;
	@Autowired
	private ProductRepository repo;
	
	@TestConfiguration
	public static class NestedTestConfig{
		
		@Bean
		public ManagerService managerService() {
			return new ManagerService();
		}
	}
	
	@BeforeEach
	public void deleteAll() {
		repo.deleteAll();
	}
	
	@Test
	public void testAddProduct() {
		
		Product p = getProduct();
		Product result = service.addProduct(p);
		assertTrue(repo.existsById(result.getId()), "Should exist after inserting");
	}
	
	@Test
	public void testEditDescrition() {
		Product p1 = getProduct();
		Product saved = service.addProduct(p1);
		String desc = "Edited test description";
		Product edited = service.editDescription(saved.getId(), desc);
		
		edited = repo.findById(edited.getId()).get();
		assertEquals(edited.getDescription(), desc,"Description should have updated");
	}

	@Test
	public void testEditPrice() {
		Product p = getProduct();
		Product saved = service.addProduct(p);
		double newPrice = 2.50;
		Product edited = service.editPrice(saved.getId(), newPrice);
		
		edited = repo.findById(edited.getId()).get();
		assertEquals(edited.getPrice(), newPrice,"Price should have updated");
	}
	
	@Test
	public void testRemoveProduct() {
		Product p = getProduct();
		Product saved = service.addProduct(p);
		service.removeProduct(saved.getId());
		assertFalse(repo.existsById(saved.getId()), "Should not exist anymore");
	}

	private Product getProduct() {
		return Product.builder().name("Test").description("Test description").price(1.50).build();
	}
}
