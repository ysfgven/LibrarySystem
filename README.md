# LibrarySystem

LibrarySystem is a simple book management application developed using **Java** and **JavaFX**.
This project is prepared as a **learning project** and allows users to add, list, view, and delete books.

## Features

* Manage printed and online books
* Add, delete, and list books
* View book details
* Prevent duplicate books with ISBN validation
* Logging system (`logs/application.log`) for error tracking
* JavaFX-based user interface
* Dynamic UI fields (fields change based on printed or online book selection)

## Requirements

* Java 17 or higher
* JavaFX SDK
* Git (optional, for version control)
* IDE (IntelliJ IDEA, Eclipse, etc.)

## Installation

1. Clone the project from GitHub or download it as a ZIP file:

```bash
git clone https://github.com/ysfgven/LibrarySystem.git
```

2. Open the project in your IDE.
3. Add JavaFX libraries to the project.
4. Run the `MainFX` class to start the application.

## Usage

* Click the **Add New Book** button to add a new book.
* Click on a book in the list to view its details.
* In the details panel, click **Delete Book** to remove a book.
* Use the **Refresh** button to update the book list.

**Note:** Log files are stored in the project root directory at `logs/application.log` and are created automatically.

## Project Structure

* `LibrarySystem/`

  * `src/` → Java source code
  * `images/` → Default icons and images, including added book covers
  * `data/` → Book and staff data files
  * `logs/` → Log files (created at runtime)
  * `.gitignore`

## Known Issues

* Some fields may not have null safety implemented.
* UI buttons and fields use fixed values (magic numbers), which may cause layout issues on different screen sizes.
* Password fields lack hashing and security measures.
* The log file (`logs/application.log`) is not included in Git; it is created automatically when needed.
* There is no file size limit for large online books, which may lead to user errors.
* ISBN validation is simple; different formats or versions may be treated as the same book.
* This project is intended for **learning and experimental purposes only** and is not suitable for commercial use.
