package com.booktrack.controller;

import com.booktrack.entity.Book;
import com.booktrack.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books") // All routes below live under /books
public class BookController {

  private final BookService service;

  public BookController(BookService service) {
    this.service = service;
  }

  // ========= LIST =========
  // GET /books?q=search
  // Loads all books; if q is present, returns a filtered list.
  @GetMapping
  public String list(@RequestParam(required = false) String q, Model model) {
    model.addAttribute("books", service.findAll(q));
    model.addAttribute("q", q);
    return "books/list"; // templates/books/list.html
  }

  // ========= CREATE (FORM) =========
  // GET /books/new
  // Shows an empty form bound to a new Book.
  @GetMapping("/new")
  public String newForm(Model model) {
    model.addAttribute("book", new Book()); // empty object for th:object
    return "books/form";                    // reuse one form for new + edit
  }

  // ========= CREATE (SUBMIT) =========
  // POST /books
  // Handles Save for NEW book (INSERT). Expect form action="/books".
  @PostMapping
  public String create(@Valid @ModelAttribute("book") Book book,
                       BindingResult br,
                       RedirectAttributes ra) {
    if (br.hasErrors()) {
      // Redisplay with validation messages
      return "books/form";
    }
    service.create(book);                 // INSERT (id is null)
    ra.addFlashAttribute("msg", "Book created");
    return "redirect:/books";             // or redirect:/books/{id}
  }

  // ========= DETAILS =========
  // GET /books/{id}
  // Read-only details view for one book.
  @GetMapping("/{id}")
  public String details(@PathVariable Long id, Model model) {
    Book book = service.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    model.addAttribute("book", book);
    return "books/details"; // templates/books/details.html
  }

  // ========= EDIT (FORM) =========
  // GET /books/{id}/edit
  // Loads existing book into the same form for editing.
  @GetMapping("/{id}/edit")
  public String edit(@PathVariable Long id, Model model) {
    Book book = service.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    model.addAttribute("book", book);
    return "books/form";
  }

  // ========= UPDATE (SUBMIT) =========
  // POST /books/{id}
  // Handles Save for EDIT (UPDATE). Expect form action="/books/{id}".
  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("book") Book book,
                       BindingResult br,
                       RedirectAttributes ra) {

    // Ensure the object carries the path id so JPA performs UPDATE, not INSERT
    book.setId(id);

    if (br.hasErrors()) {
      // On error, we return the form; hidden id in the form keeps the id intact
      return "books/form";
    }

    // Service should typically load existing, copy fields, then save
    service.update(id, book);            // UPDATE
    ra.addFlashAttribute("msg", "Book updated");
    return "redirect:/books";            // or "redirect:/books/{id}" to return to details
  }

  // ========= DELETE =========
  // POST /books/{id}/delete
  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes ra) {
    service.delete(id);
    ra.addFlashAttribute("msg", "Book deleted");
    return "redirect:/books";
  }
}
