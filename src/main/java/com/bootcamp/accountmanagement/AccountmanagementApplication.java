package com.bootcamp.accountmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountmanagementApplication.class, args);
	}

}
