package com.mspr.payetonkawasrvc;

import com.mspr.payetonkawasrvc.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PayetonkawaSrvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(PayetonkawaSrvcApplication.class, args);
	}


	@Bean
	CommandLineRunner start(ProductService productService){
		return args -> {
			productService.addProduct("Café");
			productService.addProduct("Machine à café ");
		};
	}


}
