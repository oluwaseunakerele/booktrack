package com.booktrack.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.booktrack.entity.Book;
import com.booktrack.repository.BookRepository;

@Configuration
public class DataSeeder {
  @Bean
  CommandLineRunner loadBooks(BookRepository repo) {
    return args -> {
      if (repo.count() > 0) return;

      Book b1 = new Book();
      b1.setTitle("Clean Code");
      b1.setAuthor("Robert C. Martin");
      b1.setPrice(29.99);

      Book b2 = new Book();
      b2.setTitle("Effective Java");
      b2.setAuthor("Joshua Bloch");
      b2.setPrice(39.50);

      Book b3 = new Book();
      b3.setTitle("Spring in Action");
      b3.setAuthor("Craig Walls");
      b3.setPrice(34.00);

      Book b4 = new Book();
      b4.setTitle("Design Patterns");
      b4.setAuthor("Erich Gamma et al.");
      b4.setPrice(44.99);

      repo.saveAll(List.of(b1, b2, b3, b4));
    };
  }
}
