package com.matt.sbdemo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matt.sbdemo.deals.Deal;
import com.matt.sbdemo.deals.DealManager;

@Component
public class Basket {

	/**
	 * The particular product is the key of the map and the value of the map is the
	 * quantity of the product in the basket
	 */
	private Map<Product, Integer> products;

	/**
	 * Deals are considered during the total calculation, and they process the
	 * products in the cart
	 */
	@Autowired
	private DealManager dealManager;

	public Basket() {
		products = new HashMap<>();
	}

	/**
	 * Add a product to the basket. Updates quantity if one is already present.
	 * 
	 * @param product
	 */
	public void add(Product product, int quantity) {
		Integer num = products.get(product);

		// if not found, initialize to 0
		if (num == null) {
			num = 0;
		}

		for (int i = 0; i < quantity; i++) {
			products.put(product, ++num);
		}

	}

	/**
	 * Get a view of the basket
	 * 
	 * @return a Map copy of the basket, not the original
	 */
	public Map<Product, Integer> getBasket() {
		return new HashMap<>(products);
	}

	/**
	 * Empties the whole basket
	 */
	public void empty() {
		products.clear();
	}

	/**
	 * Removes one of a particular product
	 * 
	 * @param product
	 */
	public void removeOne(Product product) {
		Integer count = products.get(product);

		if (count == 1) {
			products.remove(product);
		} else {
			products.put(product, --count);
		}
	}

	/**
	 * Removes all of a particular product
	 * 
	 * @param product
	 */
	public void removeAll(Product product) {
		products.remove(product);
	}

	/**
	 * Calculates the basket total including all discounts
	 * 
	 * @return
	 */
	public Double getTotal() {
		Double total = 0.0d;
		for (Entry<Product, Integer> e : products.entrySet()) {
			total += e.getKey().getPrice() * e.getValue();
		}

		for (Deal deal : dealManager.getActiveDeals()) {
			total = deal.applyDeal(products, total);
		}

		return total;
	}

	public void updateProduct(Product prod, int quantity) {
		products.put(prod, quantity);
	}
}
