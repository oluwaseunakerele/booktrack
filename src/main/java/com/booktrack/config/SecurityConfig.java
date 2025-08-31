package com.booktrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain security(HttpSecurity http) throws Exception {
	  http
	    .csrf(csrf -> csrf
	        .ignoringRequestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**"))
	    .authorizeHttpRequests(auth -> auth
	        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
	        .anyRequest().authenticated()
      )
      .formLogin(form -> form
          .loginPage("/login")
          .failureUrl("/login?error")
          .defaultSuccessUrl("/books", true) // <- important: not "/" or "/login"
          .permitAll()
      )
      .logout(logout -> logout
          .logoutUrl("/logout")
          .logoutSuccessUrl("/login?logout")
          .permitAll()
      );
    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager users() {
    PasswordEncoder enc = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    UserDetails admin = User.withUsername("admin")
        .password(enc.encode("admin123")) // or "{noop}admin123" if you prefer plain text for dev
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(admin);
  }
}

