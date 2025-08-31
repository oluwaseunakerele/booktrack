package com.booktrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(
  name = "books",
  indexes = {
    @Index(name = "idx_books_title", columnList = "title")
  }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 200)
  @Column(nullable = false, length = 200)
  private String title;

  @NotBlank
  @Size(max = 200)
  @Column(nullable = false, length = 200)
  private String author;

  @Size(max = 20)
  @Column(unique = true, length = 20)
  private String isbn;

  @PositiveOrZero
  @Column(nullable = false)
  private double price;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "published_on")
  private LocalDate publishedOn;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;
}
