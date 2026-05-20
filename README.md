# Library Management System (OOP Java Project)

A Java-based Library Management System developed for a university Object-Oriented Programming module. The project demonstrates strong use of OOP principles, design patterns, and file-based data persistence while simulating real-world library operations.

---

## 📌 Overview

This system allows users to manage a library of books and patrons, handle borrowing and returning of books, and maintain persistent records using text files. It is designed using a clean object-oriented architecture and implements the Command Pattern to structure user actions.

The application includes both a graphical user interface (GUI) and a structured backend logic layer.

---

## ✨ Features

- Add and manage books in the library
- Register and manage patrons
- Borrow and return books with validation rules
- Track active loans
- Persistent data storage using text files:
  - books.txt
  - loans.txt
  - patrons.txt
- Command Pattern implementation for library actions
- Graphical User Interface (GUI)
- Modular and scalable architecture

---

## 🧠 Object-Oriented Design

This project strongly applies OOP principles:

- **Encapsulation** – Data is protected within classes like Book and Patron  
- **Abstraction** – Core logic is separated from UI  
- **Inheritance** – Shared behaviour across system components  
- **Polymorphism** – Flexible command execution system  

---

## ⚙️ Design Patterns

### Command Pattern
The system uses the Command Pattern to encapsulate all library operations (e.g., borrow book, return book, add book). This allows actions to be executed dynamically and keeps the system loosely coupled and maintainable.

---

## 📁 Project Structure

```bash id="struct1"
books.txt
loans.txt
patrons.txt

commands/
data/
gui/
main/
model/
💾 Data Persistence

All library data is stored locally using text files:

books.txt → stores book records
patrons.txt → stores user/patron information
loans.txt → tracks borrowed books

This ensures data is retained between program runs.

🖥️ How to Run
Open the project in a Java IDE (IntelliJ / Eclipse / NetBeans)
Navigate to the main package
Run the main class
🎯 Learning Outcomes

This project demonstrates:

Strong understanding of object-oriented programming
Practical implementation of design patterns (Command Pattern)
GUI development in Java
File handling and persistent storage
Structured software architecture
👤 Author

Developed by Iqra J6 and other students as part of a university OOP module.
