import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Human_3 implements Externalizable {
    protected String firstName;
    protected String lastName;

    public Human_3() {
        // Default constructor for Externalizable, required for deserialization
    }

    public Human_3(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        firstName = (String) in.readObject();
        lastName = (String) in.readObject();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

class Author_3 extends Human_3 {
    public Author_3() {
        super();
    }

    public Author_3(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

class Book_3 implements Externalizable {
    private String title;
    private List<Author_3> authors;
    private int publicationYear;
    private int edition;

    public Book_3() {
    }

    public String getTitle() {
        return title;
    }

    public Book_3(String title, List<Author_3> authors, int publicationYear, int edition) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.edition = edition;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeInt(publicationYear);
        out.writeInt(edition);
        out.writeInt(authors.size());
        for (Author_3 author : authors) {
            author.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        publicationYear = in.readInt();
        edition = in.readInt();
        int authorCount = in.readInt();
        authors = new ArrayList<>();
        for (int i = 0; i < authorCount; i++) {
            Author_3 author = new Author_3();
            author.readExternal(in);
            authors.add(author);
        }
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Year: " + publicationYear + ", Edition: " + edition + ", Authors: " + authors;
    }
}

class Library_3 implements Externalizable {
    private String name;
    private List<Book_3> books;

    public Library_3() {
    }

    public Library_3(String name, List<Book_3> books) {
        this.name = name;
        this.books = books;
    }

    public List<Book_3> getBooks() {
        return books;
    }

    public void addBook(Book_3 book) {
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(books.size());
        for (Book_3 book : books) {
            book.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        int bookCount = in.readInt();
        books = new ArrayList<>();
        for (int i = 0; i < bookCount; i++) {
            Book_3 book = new Book_3();
            book.readExternal(in);
            books.add(book);
        }

    }

    @Override
    public String toString() {
        return "Library: " + name + ", Books: " + books;
    }
}

class LibraryManagement_3 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "library_externalizable.ser";
    private static Library_3 library = new Library_3("City Library", new ArrayList<>());

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
                    library = (Library_3) deSerializeObject(FILE_NAME);
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
        scanner.nextLine();
        System.out.print("Enter edition: ");
        int edition = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter author's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter author's last name: ");
        String lastName = scanner.nextLine();
        Author_3 author = new Author_3(firstName, lastName);
        Book_3 newBook = new Book_3(title, Arrays.asList(author), year, edition);
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
