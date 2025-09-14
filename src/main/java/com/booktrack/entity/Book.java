package com.booktrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * Book JPA entity.
 *
 * Notes on ISBN:
 * - We keep a UNIQUE index on isbn.
 * - Blank/whitespace ISBNs are normalized to NULL in @PrePersist/@PreUpdate.
 *   MySQL allows multiple NULLs in a UNIQUE column, so optional ISBNs won't
 *   violate the unique constraint.
 */
@Entity
@Table(
  name = "books",
  indexes = {
    @Index(name = "idx_books_title", columnList = "title")
  }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {

  // ---- Primary Key ----
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // ---- Required fields ----
  @NotBlank
  @Size(max = 200)
  @Column(nullable = false, length = 200)
  private String title;

  @NotBlank
  @Size(max = 200)
  @Column(nullable = false, length = 200)
  private String author;

  // ---- ISBN (Optional but Unique) ----
  // - Leave nullable=true (default) so multiple NULLs are allowed.
  // - We trim and convert "" to NULL in lifecycle callbacks below.
  // - Add a light format guard if you want (example @Pattern commented out).
  @Size(max = 20)
  @Column(name = "isbn", unique = true, length = 20)
  // @Pattern(regexp = "^[0-9\\-Xx]*$", message = "ISBN can only contain digits, dashes, or X")
  private String isbn;

  // ---- Price ----
  // Using double is OK for demos, but BigDecimal is safer for money.
  @PositiveOrZero
  @Column(nullable = false)
  private double price;

  // ---- Dates ----
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "published_on")
  private LocalDate publishedOn;

  // ---- Description ----
  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;

  // ---- Normalize fields before INSERT/UPDATE ----
  @PrePersist
  @PreUpdate
  private void normalize() {
    // Trim common string fields
    if (title != null)   title = title.trim();
    if (author != null)  author = author.trim();

    // ISBN: trim and convert blank to NULL so it won't violate UNIQUE
    if (isbn != null) {
      isbn = isbn.trim();
      if (isbn.isEmpty()) {
        isbn = null; // <— key line: avoid writing '' which breaks UNIQUE
      }
    }
    // (Optional) trim description without converting to null
    if (description != null) description = description.trim();
  }
}
