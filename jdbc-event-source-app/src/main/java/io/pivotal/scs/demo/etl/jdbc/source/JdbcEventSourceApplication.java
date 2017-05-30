package io.pivotal.scs.demo.etl.jdbc.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdbcEventSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcEventSourceApplication.class, args);
	}
}
