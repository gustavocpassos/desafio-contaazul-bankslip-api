package br.com.gustavocpassos.contaazul.bankslip.api;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class BankslipsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankslipsApiApplication.class, args);
	}

	@Bean
	public Docket swagger2Configurer() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("Bankslip REST API - Desafio Conta Azul")
				.description("REST API Documentarion<br> Desafio Conta Azul").build();

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select().build();
	}

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
