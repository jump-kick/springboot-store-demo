package com.matt.sbdemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.sbdemo.model.ApplyDealRequest;
import com.matt.sbdemo.model.Product;

public class TestUtils {

	public static Product getProductA() {
		return Product.builder().name("Widget")
				.description("It's widgety").price(15.00d).build();
	}
	
	public static Product getProductA_FixedId() {
		Product p = getProductA();
		p.setId(1L);
		return p;
	}
	
	public static Product getProductB() {
		return Product.builder().name("Doohickey").description("It does...something.")
				.price(9.00d).build();
	}
	
	public static Product getProductB_FixedId() {
		Product p = getProductB();
		p.setId(2L);
		return p;
	}
	
	public static Product getProductC() {
		return Product.builder().name("Knick-knack")
				.description("It's purposeless").price(150.00d).build();
	}
	
	public static Product getProductC_FixedId() {
		Product p = getProductC();
		p.setId(3L);
		return p;
	}
	
	public static ApplyDealRequest getBogo50Request() {
		return ApplyDealRequest.builder().discountCode("BOGO50").id1(1L).build();
	}
	
	public static ApplyDealRequest getBogoBundleRequest() {
		return ApplyDealRequest.builder().discountCode("BOGOBUNDLE").id1(1L).id2(2L).build();
	}
	
	public static String getProductJson() {
		return getProductJson(getProductA());
	}
	
	public static String getProductJson(Product p) {
		
		String value = null;
		try {
			value = new ObjectMapper().writeValueAsString(p);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return value;
	}
}
