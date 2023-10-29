package com.matt.sbdemo.deals;

/**
 * Abstract class allows us to require the single product deal to have an ID,
 * i.e., the deal applies to a single type of product.
 * @author matthew bankert
 *
 */
public abstract class SingleProductDeal implements Deal {

	private Long id;

	public SingleProductDeal(Long id) {
		this.setId(id);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
