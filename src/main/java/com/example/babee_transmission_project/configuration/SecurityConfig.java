//package com.example.babee_transmission_project.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//				.csrf().disable()
//				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/", "/static/**").permitAll()
//						.anyRequest().authenticated()
//				);
//		return http.build();
//	}
//}
