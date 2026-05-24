# LibrarySystem

> A learning project — but one that grew up -a bit-.

LibrarySystem started as a simple JavaFX experiment to get comfortable with desktop UI development.
What began as a handful of buttons and hardcoded layouts eventually turned into a proper refactoring journey:
MVC architecture, dependency injection, interface-based services, abstract models, and a real MySQL backend.


**It is not a finished product. It is not trying to be one.
This was my first project built with a UI framework. The original version stored
all data in CSV files and had no real architecture to speak of — it was purely a
sandbox for getting comfortable with JavaFX. Over time it became the project where
I first practiced MVC in a real codebase, wired up a database, and learned what
dependency injection actually means when you have to do it yourself.Know that refactored a lot but its still a bit mess**
---

## Tech Stack

- Java 23
- JavaFX 23
- MySQL (via JDBC)
- Maven

---

## Features

- Add, list, view, and delete books
- Two book types: **Printed** and **Online** with dynamic UI fields
- ISBN-based duplicate prevention
- Optional cover image per book
- Centralized error handling and logging (`logs/application.log`)
- Staff login system

---

## Requirements

- Java 17 or higher
- Maven
- MySQL Server
- An IDE (IntelliJ IDEA recommended)

---

## Installation

1. Clone the repository:

```bash
git clone https://github.com/ysfgven/LibrarySystem.git
```

2. Create the database using the provided SQL script:

```bash
mysql -u root -p < library_db.sql
```

3. Configure your database credentials in `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/library_db
db.user=your_username
db.password=your_password
```

4. Run the project via Maven:

```bash
mvn javafx:run
```

---

## Usage

- Click **Add New Book** to add a book
- Click a book in the list to view its details
- Click **Delete Book** in the detail panel to remove a book
- Click **Refresh** to reload the book list
- A cover image can optionally be assigned to each book after saving

---

## Project Structure

```
LibrarySystem/
├── src/main/java/
│   ├── MainFX.java
│   ├── controller/       → MVC controllers
│   ├── model/            → Book (abstract), PrintedBook, OnlineBook, Staff
│   ├── service/          → IBookHandler, BookHandler, IStaffHandler, StaffHandler, ImageManager, AssetsHandler
│   ├── db/               → DatabaseManager
│   ├── view/             → JavaFX UI classes
│   └── util/             → ErrorHandler, LogHelper
├── src/main/resources/
│   └── db.properties     → Database config (not committed to Git create on your own)
├── images/               → Book covers and app icon
├── logs/                 → Runtime log files (auto-created)
└── library_db.sql        → Database schema
```

---

## Known Limitations

These are known, understood, and intentionally left as-is for a JavaFX learning project:

- **No password hashing** — plain text for now; BCrypt would be the real-world fix
- **No connection pooling** — per-query connections work at this scale; HikariCP is the next step
- **No pagination or search** — full list loads on every refresh
- **No book update** — add and delete only
- **Absolute UI positioning** — layouts use `setLayoutX/Y` and won't scale across screen sizes
- **Basic ISBN validation** — duplicate check only, no format or checksum validation

---

## Notes

- The `images/` folder must contain `icon.png` and `defaultBook.png` at runtime — the app checks for these on startup and exits if they are missing
- This project is for **learning purposes only** 
