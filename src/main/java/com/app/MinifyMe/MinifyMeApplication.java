package com.app.MinifyMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MinifyMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinifyMeApplication.class, args);
	}

}
