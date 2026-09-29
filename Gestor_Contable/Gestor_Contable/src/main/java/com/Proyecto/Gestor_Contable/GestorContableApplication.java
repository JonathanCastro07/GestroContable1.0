package com.Proyecto.Gestor_Contable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestorContableApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorContableApplication.class, args);
	}

}