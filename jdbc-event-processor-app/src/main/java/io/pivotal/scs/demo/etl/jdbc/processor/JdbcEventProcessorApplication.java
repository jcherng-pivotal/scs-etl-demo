package io.pivotal.scs.demo.etl.jdbc.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdbcEventProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcEventProcessorApplication.class, args);
	}
}
