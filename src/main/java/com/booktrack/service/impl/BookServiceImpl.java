package com.booktrack.service.impl;

import com.booktrack.entity.Book;
import com.booktrack.repository.BookRepository;
import com.booktrack.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

  private final BookRepository repo;

  public BookServiceImpl(BookRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Book> findAll(String q) {
    return (q == null || q.isBlank())
        ? repo.findAll()
        : repo.findByTitleContainingIgnoreCase(q);
  }

  @Override
  public Optional<Book> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public Book get(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
  }

  @Override
  public Book create(Book b) {
    return repo.save(b);
  }

  @Override
  public Book update(Long id, Book b) {
    Book cur = get(id);
    cur.setTitle(b.getTitle());
    cur.setAuthor(b.getAuthor());
    cur.setIsbn(b.getIsbn());
    cur.setPrice(b.getPrice());
    cur.setPublishedOn(b.getPublishedOn());
    cur.setDescription(b.getDescription());
    return cur; // JPA will flush changes
  }

  @Override
  public void delete(Long id) {
    repo.deleteById(id);
  }
}
