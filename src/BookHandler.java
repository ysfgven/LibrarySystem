import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class BookHandler {
    private final Path filePath = Paths.get("data", "bookList.csv");

    private final List<Book> books = new ArrayList<>();

    public BookHandler() {
        createCSVFile();
        readCSVFile();
    }

    public void createCSVFile() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) Files.createDirectories(parent);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            ErrorHandler.showErrorAlert("Error", "Create Error", "There was an error while creating the file", false);
            LogHelper.logException(e);
        }
    }

    public List<Book> getBookList() {
        return books;
    }

    public void refreshFromDisk() {
        readCSVFile();
    }

    public Book getBookByTitle(String title) {
        if (title == null) return null;
        for (Book book : books) {
            if (book.getTitle() != null && book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public String toCSVLine(Book book) {
        if (book instanceof PrintedBook printed) {
            return String.join(",",
                    "PRINTED",
                    escapeCsv(printed.getTitle()),
                    escapeCsv(printed.getAuthor()),
                    escapeCsv(printed.getPublisher()),
                    escapeCsv(printed.getIsbn()),
                    escapeCsv(printed.getSummary()),
                    escapeCsv(printed.getShelfLocation()),
                    escapeCsv(printed.getCoverType())
            );
        } else if (book instanceof OnlineBook online) {
            return String.join(",",
                    "ONLINE",
                    escapeCsv(online.getTitle()),
                    escapeCsv(online.getAuthor()),
                    escapeCsv(online.getPublisher()),
                    escapeCsv(online.getIsbn()),
                    escapeCsv(online.getSummary()),
                    String.valueOf(online.getFileSize()),
                    escapeCsv(online.getFormat())
            );
        } else {
            throw new IllegalArgumentException("Unknown book type : " + book.getClass().getName());
        }
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\n") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    public void addBooksAtomically()  {
        try {
            Path parent = filePath.getParent();
            if (parent == null) {
                throw new IOException("Parent directory is null for file: " + filePath);
            }
            Files.createDirectories(parent);
            Path temp = Files.createTempFile(parent, "booklist-", ".tmp");
            try (BufferedWriter bw = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                for (Book book : books) {
                    bw.write(toCSVLine(book));
                    bw.newLine();
                }
            }

            Files.move(temp, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            ErrorHandler.showErrorAlert("Error", "Writing Error", "There was an error while writing the file", false);
            LogHelper.logException(e);
        }
    }

    public boolean deleteBookByTitle(String title, String isbn) {

        if (title == null || isbn == null) return false;

        String titleTrim = title.trim();
        String isbnTrim = isbn.trim();

        Book found = null;
        for (Book b : books) {
            if (b.getTitle() == null || b.getIsbn() == null) continue;
            if (b.getTitle().trim().equalsIgnoreCase(titleTrim)
                    && b.getIsbn().trim().equalsIgnoreCase(isbnTrim)) {
                found = b;
                break;
            }
        }

        if (found == null) return false;

        books.remove(found);
        ImageManager.deleteBookImage(isbn);


        addBooksAtomically();
        return true;
    }

    private String[] splitCsvLine(String line) {
        if (line == null) return new String[0];
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }

    public void readCSVFile() {
        List<String> readedList;
        books.clear();


        try {
            if (Files.exists(filePath)) {
                readedList = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            } else {
                return;
            }
        } catch (IOException e) {
            ErrorHandler.showErrorAlert("Error", "Read Error", "There was an error reading the file. ", false);
            LogHelper.logException(e);
            return;
        }

        int lineNo = 0;
        for (String line : readedList) {
            lineNo++;
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] lineArray = splitCsvLine(line);

            if (lineArray.length < 8) {
                LogHelper.logException(new Exception("Invalid line has skipped, LineNumber:" + lineNo + ": " + line));
                continue;
            }

            for (int i = 0; i < lineArray.length; i++) {
                if (lineArray[i] != null) lineArray[i] = lineArray[i].trim();
            }
            BookType type;
            try {
                type = BookType.valueOf(lineArray[0].trim());
            } catch (NullPointerException e) {
                LogHelper.logException(new Exception("Unknown book type skipped LineNumber:" + lineNo + ": " + lineArray[0]));
                continue;
            }

            try {
                if (type == BookType.PRINTED) {
                    PrintedBook printedBook = new PrintedBook(
                            lineArray[1],
                            lineArray[2],
                            lineArray[3],
                            lineArray[4],
                            lineArray[5],
                            BookType.PRINTED,
                            lineArray[6],
                            lineArray[7]
                    );
                    books.add(printedBook);
                } else { // ONLINE
                    int fileSize = 0;
                    try {
                        fileSize = Integer.parseInt(lineArray[6].trim());
                    } catch (NumberFormatException nfe) {
                        LogHelper.logException(new Exception("File size could not converted LineNumber:" + lineNo + "): " + line));
                    }
                    OnlineBook onlineBook = new OnlineBook(
                            lineArray[1],
                            lineArray[2],
                            lineArray[3],
                            lineArray[4],
                            lineArray[5],
                            BookType.ONLINE,
                            fileSize,
                            lineArray[7]
                    );
                    books.add(onlineBook);
                }
            } catch (Exception e) {
                LogHelper.logException(new Exception("Error in line process LineNumber:" + lineNo + ": " + e.getMessage(), e));
            }
        }
    }

    public Book getBookByIsbn(String isbn) {
        if (isbn == null) return null;

        String isbnTrim = isbn.trim();
        if (isbnTrim.isEmpty())
            return null;

        for (Book book : books) {
            String bookIsbn = book.getIsbn();
            if (bookIsbn != null && bookIsbn.trim().equalsIgnoreCase(isbnTrim)) {
                return book;
            }
        }
        return null;
    }
}
