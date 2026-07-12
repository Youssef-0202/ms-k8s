package com.demo.gateway_keycloak;


import com.demo.gateway_keycloak.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
@EntityScan(basePackages = "com.demo.gateway_keycloak.model")
@EnableJpaRepositories(basePackages = "com.demo.gateway_keycloak.repository")
public class GatewayKeycloakApplication {

	@Autowired
	private RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(GatewayKeycloakApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(){
		return args ->{
			  roleService.initializeDefaultRoles();
              log.info("Default roles initialized successfully!");
              log.info("Database ready for user synchronization");
		};
	}


}
