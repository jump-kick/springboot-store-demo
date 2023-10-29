package com.matt.sbdemo.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matt.sbdemo.data.ProductRepository;
import com.matt.sbdemo.model.Product;

@Service
public class ManagerService {
	
	@Autowired
	private ProductRepository repo;

	public Product addProduct(Product product) {
		return repo.save(product);
	}

	public Product editDescription(Long id, String description) {
		Product p = repo.findById(id).get();
		p.setDescription(description);
		return repo.save(p);
	}

	public Product editPrice(Long id, double newPrice) {
		Product p = repo.findById(id).get();
		p.setPrice(newPrice);
		return repo.save(p);
	}

	public void removeProduct(Long id) {
		repo.deleteById(id);
	}
}
