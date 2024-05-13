import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Author {
    private String name;
    private int age;

    public Author(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class Book {
    private String name;
    private List<Author> authors;

    public Book(String name, List<Author> authors) {
        this.name = name;
        this.authors = authors;
    }

    public String getName() {
        return name;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", authors=" + authors +
                '}';
    }
}

class Bookshelf implements Serializable {
    private transient List<Book> books;

    public Bookshelf() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the bookshelf.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(books.size());
        for (Book b : books) {
            out.writeObject(b.getName());
            List<Author> authors = b.getAuthors();
            out.writeInt(authors.size());
            for (Author a : authors) {
                out.writeObject(a.getName());
                out.writeInt(a.getAge());
            }
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        books = new ArrayList<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String bookName = (String) in.readObject();
            int authorSize = in.readInt();
            List<Author> authors = new ArrayList<>();
            for (int j = 0; j < authorSize; j++) {
                String authorName = (String) in.readObject();
                int authorAge = in.readInt();
                authors.add(new Author(authorName, authorAge));
            }
            books.add(new Book(bookName, authors));
        }
    }

    public void save(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    public static Bookshelf load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Bookshelf) in.readObject();
        }
    }
}

class LibraryManager_2 {
    private static final Scanner scanner = new Scanner(System.in);
    private static Bookshelf bookshelf = new Bookshelf();

    public static void main(String[] args) {
        String filename = "bookshelf.ser";
        try {
            bookshelf = Bookshelf.load(filename);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing bookshelf found, starting a new one.");
        }

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add a book");
            System.out.println("2. Display all books");
            System.out.println("3. Save and exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume the newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    bookshelf.displayBooks();
                    break;
                case 3:
                    try {
                        bookshelf.save(filename);
                        System.out.println("Data saved. Exiting.");
                        System.exit(0);
                    } catch (IOException ex) {
                        System.out.println("Error saving the bookshelf.");
                    }
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    break;
            }
        }
    }

    private static void addBook() {
        System.out.print("Enter book name: ");
        String bookName = scanner.nextLine();
        List<Author> authors = new ArrayList<>();
        System.out.print("How many authors does the book have? ");
        int numAuthors = scanner.nextInt();
        scanner.nextLine();  // consume the newline
        for (int i = 0; i < numAuthors; i++) {
            System.out.print("Enter author's name: ");
            String name = scanner.nextLine();
            System.out.print("Enter author's age: ");
            int age = scanner.nextInt();
            scanner.nextLine();  // consume the newline
            authors.add(new Author(name, age));
        }
        bookshelf.addBook(new Book(bookName, authors));
        System.out.println("Book added successfully!");
    }
}
