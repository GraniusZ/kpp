import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Human_1 implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String firstName;
    protected String lastName;

    public Human_1(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

class Author_1 extends Human_1 {
    public Author_1(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

class Book_1 implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private List<Author_1> authors;
    private int publicationYear;
    private int edition;

    public Book_1(String title, List<Author_1> authors, int publicationYear, int edition) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Authors: " + authors + ", Year: " + publicationYear + ", Edition: " + edition;
    }
}

class BookReader_1 extends Human_1 implements Serializable {
    private static final long serialVersionUID = 1L;
    private int registrationNumber;
    private List<Book_1> borrowedBooks;

    public BookReader_1(String firstName, String lastName, int registrationNumber, List<Book_1> borrowedBooks) {
        super(firstName, lastName);
        this.registrationNumber = registrationNumber;
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public String toString() {
        return super.toString() + ", Registration No: " + registrationNumber + ", Borrowed: " + borrowedBooks;
    }
}

class Library_1 implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Book_1> books;

    public Library_1(String name, List<Book_1> books) {
        this.name = name;
        this.books = books;
    }

    public void addBook(Book_1 book) {
        books.add(book);
        System.out.println("Book added successfully!");
    }

    public void removeBook(String title) {
        if (books.removeIf(book -> book.getTitle().equals(title))) {
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    @Override
    public String toString() {
        return "Library: " + name + ", Books: " + books;
    }
}

class LibraryManagement_1 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "library.ser";
    private static Library_1 library = new Library_1("City Library", new ArrayList<>());

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1. Display Library Information");
            System.out.println("2. Add Book");
            System.out.println("3. Remove Book");
            System.out.println("4. Serialize Library");
            System.out.println("5. Deserialize Library");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println(library);
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    removeBook();
                    break;
                case 4:
                    serializeObject(FILE_NAME, library);
                    System.out.println("Library serialized successfully.");
                    break;
                case 5:
                    library = (Library_1) deSerializeObject(FILE_NAME);
                    System.out.println("Library deserialized successfully.");
                    break;
                case 6:
                    System.out.println("Exiting the system.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter again.");
            }
        } while (choice != 6);
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = scanner.nextInt();
        System.out.print("Enter edition: ");
        int edition = scanner.nextInt();
        scanner.nextLine(); // consume the newline
        System.out.print("Enter author's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter author's last name: ");
        String lastName = scanner.nextLine();
        Author_1 author = new Author_1(firstName, lastName);
        Book_1 newBook = new Book_1(title, Arrays.asList(author), year, edition);
        library.addBook(newBook);
    }

    private static void removeBook() {
        System.out.print("Enter the title of the book to remove: ");
        String title = scanner.nextLine();
        library.removeBook(title);
    }

    public static void serializeObject(String fileName, Object obj) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName))) {
            os.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deSerializeObject(String fileName) {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName))) {
            return is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
