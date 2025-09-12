package com.booktrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  // === Hard-coded credentials ===
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails admin = User.withUsername("admin")
        // plain text for dev: "admin123"
        .password("{noop}admin123")
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(admin);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/login", "/css/**", "/webjars/**").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/login").permitAll()
        .loginProcessingUrl("/login")        // <- explicit
        .defaultSuccessUrl("/books", true)
        .failureUrl("/login?error")
      )
      .logout(l -> l.logoutSuccessUrl("/login?logout").permitAll());
    return http.build();
  }

}
