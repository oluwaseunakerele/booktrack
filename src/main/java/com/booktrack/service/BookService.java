package com.booktrack.service;

import com.booktrack.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
  List<Book> findAll(String q);
  Optional<Book> findById(Long id);   // <— added
  Book get(Long id);                  // keep if you already use it elsewhere
  Book create(Book b);
  Book update(Long id, Book b);
  void delete(Long id);
}
