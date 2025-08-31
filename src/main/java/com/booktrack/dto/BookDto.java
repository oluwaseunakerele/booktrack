package com.booktrack.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookDto {
  private Long id;
  private String title;
  private String author;
  private String isbn;
  private double price;
  private LocalDate publishedOn;
  private String description;
}
