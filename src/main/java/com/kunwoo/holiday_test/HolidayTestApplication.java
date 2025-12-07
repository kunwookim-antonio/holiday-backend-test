package com.kunwoo.holiday_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HolidayTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolidayTestApplication.class, args);
	}

}
