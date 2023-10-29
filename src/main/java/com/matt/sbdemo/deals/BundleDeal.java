package com.matt.sbdemo.deals;

import java.util.Map;

import com.matt.sbdemo.model.Product;

/**
 * Abstract class specifies that bundle deals should operate on
 * two different products, where the IDs are id1 and id2.
 * @author matthew bankert
 *
 */
public abstract class BundleDeal implements Deal {

	private Long id1;
	private Long id2;
	
	public BundleDeal(Long id1, Long id2) {
       setIDs(id1, id2);
	}
	
	public void setIDs(Long id1, Long id2) {
		this.id1 = id1;
		this.id2 = id2;
	}
	
	public Long getID1() {
		return id1;
	}
	
	public Long getID2() {
		return id2;
	}

}
