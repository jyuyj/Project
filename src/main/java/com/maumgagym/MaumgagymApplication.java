package com.maumgagym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( basePackages = { "com.maumgagym" })
public class MaumgagymApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaumgagymApplication.class, args);
	}

}
