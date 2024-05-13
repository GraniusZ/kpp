import java.io.*;
import java.net.*;
import java.util.Scanner;

class MetroCardClient {
    private String address;
    private int port;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner scanner;

    public MetroCardClient(String address, int port) {
        this.address = address;
        this.port = port;
        scanner = new Scanner(System.in);
    }

    public void connect() throws IOException {
        socket = new Socket(address, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        System.out.println("Connected to the server at " + address + ":" + port);
    }

    public void sendRequest(String action, String cardId, int amount) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Reconnecting to server...");
            connect();
        }
        out.writeUTF(action);
        out.writeUTF(cardId);
        if (action.equals("topup") || action.equals("pay")) {
            out.writeInt(amount);
        }
        handleResponse();
    }

    public void sendRegisterRequest(String cardId, String name) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Reconnecting to server...");
            connect();
        }
        out.writeUTF("register");
        out.writeUTF(cardId);
        out.writeUTF(name); // Send the name for registration
        handleResponse();
    }

    private void handleResponse() throws IOException {
        String response = in.readUTF();
        System.out.println(response);
    }


    public void showMenu() {
        try {
            connect(); // Initial connection
            while (true) {
                System.out.println("\nPlease choose an option:");
                System.out.println("1. Register new card");
                System.out.println("2. Get card info");
                System.out.println("3. Top up balance");
                System.out.println("4. Pay for ride");
                System.out.println("5. Check balance");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        System.out.print("Enter a new card ID: ");
                        String newCardId = scanner.nextLine();
                        System.out.print("Enter your name: ");
                        String name = scanner.nextLine();
                        sendRegisterRequest(newCardId, name);
                        break;
                    case 2:
                        System.out.print("Enter card ID: ");
                        String cardId = scanner.nextLine();
                        sendRequest("info", cardId, 0);
                        break;
                    case 3:
                        System.out.print("Enter card ID: ");
                        cardId = scanner.nextLine();
                        System.out.print("Enter amount to top up: ");
                        int amount = scanner.nextInt();
                        sendRequest("topup", cardId, amount);
                        break;
                    case 4:
                        System.out.print("Enter card ID: ");
                        cardId = scanner.nextLine();
                        System.out.print("Enter amount to pay: ");
                        amount = scanner.nextInt();
                        sendRequest("pay", cardId, amount);
                        break;
                    case 5:
                        System.out.print("Enter card ID: ");
                        cardId = scanner.nextLine();
                        sendRequest("balance", cardId, 0);
                        break;
                    case 0:
                        close();
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to connect to server: " + e.getMessage());
        }
    }

    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
        scanner.close();
    }

    public static void main(String[] args) {
        MetroCardClient client = new MetroCardClient("localhost", 12345);
        client.showMenu();
    }
}
