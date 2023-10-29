package com.matt.sbdemo.data;

import org.springframework.data.repository.CrudRepository;

import com.matt.sbdemo.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
