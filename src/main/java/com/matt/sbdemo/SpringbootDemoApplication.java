package com.matt.sbdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matt.sbdemo.deals.Bogo50PercentDeal;
import com.matt.sbdemo.deals.BogoBundleDeal;
import com.matt.sbdemo.deals.DealManager;

@SpringBootApplication
public class SpringbootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDemoApplication.class, args);
	}
	
	@Bean
	public DealManager dealManager() {
		DealManager dm = new DealManager();
		dm.addDeal(new Bogo50PercentDeal());
		dm.addBundleDeal(new BogoBundleDeal());
		return dm;
	}
}
