package com.booktrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;

@Configuration
public class SecurityConfig {

  // ---- Hard-coded credentials (dev only) ----
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails admin = User.withUsername("admin")
        .password("{noop}admin123")   // plain text for dev
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(admin);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
          // public assets + login page
          .requestMatchers("/", "/login", "/css/**", "/webjars/**").permitAll()
          // actuator health/info should be publicly reachable for monitoring
          .requestMatchers("/actuator/health", "/actuator/info").permitAll()
          .anyRequest().authenticated()
      )
      .formLogin(form -> form
          .loginPage("/login").permitAll()
          .loginProcessingUrl("/login")
          .defaultSuccessUrl("/books", true)
          .failureUrl("/login?error")
      )
      .logout(l -> l.logoutSuccessUrl("/login?logout").permitAll());

    return http.build();
  }

  // Expose /actuator/httpexchanges data (optional but useful for the milestone)
  @Bean
  public InMemoryHttpExchangeRepository httpExchangesRepo() {
    return new InMemoryHttpExchangeRepository();
  }
}

