package com.unigem.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@RestController
@SpringBootApplication
@Configuration
//@EnableSwagger2
public class ServerApplication {

	//@RequestMapping("/")
	//public String home() {
	//	return "Welcome to Unigem Server";
	//}

	public static void main(String[] args) {
		try {
			SpringApplication.run(ServerApplication.class, args);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
