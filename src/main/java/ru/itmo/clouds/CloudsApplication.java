package ru.itmo.clouds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
class CloudsApplication extends SpringBootServletInitializer {
	public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CloudsApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(CloudsApplication.class);
	}

}

