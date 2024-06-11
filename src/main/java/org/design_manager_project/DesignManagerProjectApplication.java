package org.design_manager_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesignManagerProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignManagerProjectApplication.class, args);
	}

}
