  ## BookTrack

A simple Spring Boot 3 + Thymeleaf application for tracking books.
This is the Milestone 2 deliverable: local build, CRUD, theming, and login, ready to run on a developer laptop.

## Overview

BookTrack lets a signed-in user:

view a list of books,

search by title,

create, edit, and delete books,

see a book’s detail page.

The UI is built with Thymeleaf templates and a lightweight custom CSS theme (no JS framework).
Security is handled by Spring Security with an in-memory admin user for local development.

## Features

🔐 Login form (/login) → protected routes

📚 Books list with search (/books)

➕ Add book (/books/new)

✏️ Edit book (/books/{id}/edit)

👁️ Details (/books/{id})

🗑️ Delete with POST (/books/{id}/delete)

🎨 Themed pages: gradient title, zebra rows, hover highlighting

🌱 Startup data via a DataSeeder so the table isn’t empty

## Tech stack

Java 17, Spring Boot 3.2+

Spring MVC, Thymeleaf, Spring Security

Spring Data JPA (MySQL)

Maven

## Architecture (very small, layered)
Controller  ->  Service  ->  Repository  ->  Entity (JPA/Hibernate)


## Core classes

Book (entity)

BookRepository (Spring Data JPA)

BookService, BookServiceImpl

BookController (Thymeleaf MVC)

SecurityConfig, MvcRedirects, DataSeeder

## App flow / site map
/login  →  /books (list)  →  /books/{id} (details)
           ├─ /books/new
           └─ /books/{id}/edit
           ↩  back to list

## Running locally

Prereqs

JDK 17+

Maven 3.9+

MySQL 8.x (or 5.7) running locally with a database created (e.g. booktrack)

# 1) Configure local DB (do NOT commit secrets)
Create src/main/resources/application-local.properties:

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/booktrack?serverTimezone=UTC&useSSL=false
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASS
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080


application-local.properties is git-ignored so credentials never leave your machine.

# 2) Run

mvn spring-boot:run


# 3) Sign in

Username: admin
Password: admin123

## Default routes
Method	Path	Purpose
GET	/login	Login page
GET	/books	List + search
GET	/books/new	Create form
POST	/books	Create
GET	/books/{id}	Details
GET	/books/{id}/edit	Edit form
POST	/books/{id}	Update
POST	/books/{id}/delete	Delete
## UI at a glance

Login: centered card, gradient header, inline error on bad credentials.

Books list: search box, zebra table rows, action buttons (View/Edit/Delete).

Book form: create/edit, validation on title/author, Save/Cancel.

## Sample data

On first run, DataSeeder inserts a few books (Effective Java, Spring in Action, Design Patterns) if the table is empty.

## Project layout
src/main/java/com/booktrack/
  BooktrackApplication.java
  config/  (SecurityConfig, MvcRedirects, DataSeeder)
  controller/BookController.java
  entity/Book.java
  repository/BookRepository.java
  service/ (BookService, impl/)
src/main/resources/
  templates/books/ (list.html, form.html, details.html)
  templates/login.html
  static/css/app.css
  application.properties
  application-local.properties  (ignored)

## Milestone checklist

 Local build + run

 Login protection

 CRUD for Book (Thymeleaf)

 Basic theming

 Data seeding

 Git repository pushed (main branch)

Troubleshooting

Port in use (8080): change server.port in application-local.properties.

Login fails: ensure you’re using admin / admin123.

DB errors: confirm DB is running, credentials are correct, and you created the booktrack database.

## Links

Repository: https://github.com/oluwaseunakerele/booktrack

Screencast: (add your unlisted YouTube/Stream link here)

## Quick commit & push
git add README.md
git commit -m "docs: add project overview and run instructions"
git push
