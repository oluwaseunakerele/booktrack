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
@RequestMapping("/books")
public class BookController {

  private final BookService service;

  public BookController(BookService service) {
    this.service = service;
  }

  // LIST
  @GetMapping
  public String list(@RequestParam(required = false) String q, Model model) {
    model.addAttribute("books", service.findAll(q));
    model.addAttribute("q", q);
    return "books/list";
  }

  // CREATE FORM
  @GetMapping("/new")
  public String newForm(Model model) {
    model.addAttribute("book", new Book());
    return "books/form";
  }

  // CREATE (Save)
  @PostMapping
  public String create(@Valid @ModelAttribute("book") Book book,
                       BindingResult br,
                       RedirectAttributes ra) {
    if (br.hasErrors()) {
      return "books/form";
    }
    service.create(book);
    ra.addFlashAttribute("msg", "Book created");
    return "redirect:/books";
  }

  // DETAILS
  @GetMapping("/{id}")
  public String details(@PathVariable Long id, Model model) {
    Book book = service.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    model.addAttribute("book", book);
    return "books/details";
  }

  // EDIT FORM
  @GetMapping("/{id}/edit")
  public String edit(@PathVariable Long id, Model model) {
    Book book = service.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    model.addAttribute("book", book);
    return "books/form";
  }

  // UPDATE (Save)
  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("book") Book book,
                       BindingResult br,
                       RedirectAttributes ra) {
    // make sure the entity carries the path id
    book.setId(id);

    if (br.hasErrors()) {
      // when we return to the form on error, book.id stays populated thanks to setId + hidden field
      return "books/form";
    }
    service.update(id, book);
    ra.addFlashAttribute("msg", "Book updated");
    return "redirect:/books";
  }

  // DELETE
  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes ra) {
    service.delete(id);
    ra.addFlashAttribute("msg", "Book deleted");
    return "redirect:/books";
  }
}
