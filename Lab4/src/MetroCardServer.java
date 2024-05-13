import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MetroCardServer {
    private ServerSocket serverSocket;
    private Map<String, UserCardInfo> cards = new HashMap<>();

    private static class UserCardInfo {
        int balance;
        String name;

        UserCardInfo(String name) {
            this.name = name;
            this.balance = 0;
        }
    }

    public MetroCardServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);
    }

    public void start() {
        while (true) {
            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                while (!socket.isClosed()) {
                    String action = in.readUTF();
                    String cardId = in.readUTF();
                    handleRequest(action, cardId, in, out);
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(String action, String cardId, DataInputStream in, DataOutputStream out) throws IOException {
        switch (action) {
            case "register":
                String name = in.readUTF(); // Read the name
                cards.put(cardId, new UserCardInfo(name));
                out.writeUTF("Card registered with ID: " + cardId + " for " + name);
                break;
            case "info":
                if (cards.containsKey(cardId)) {
                    UserCardInfo info = cards.get(cardId);
                    out.writeUTF("Card ID: " + cardId + ", Name: " + info.name + ", Balance: " + info.balance);
                } else {
                    out.writeUTF("Card not found.");
                }
                break;
            case "topup":
                int amount = in.readInt();
                if (cards.containsKey(cardId)) {
                    UserCardInfo info = cards.get(cardId);
                    info.balance += amount;
                    out.writeUTF("Added " + amount + " to card ID: " + cardId);
                } else {
                    out.writeUTF("Card not found.");
                }
                break;
            case "pay":
                int cost = in.readInt();
                if (cards.containsKey(cardId) && cards.get(cardId).balance >= cost) {
                    UserCardInfo info = cards.get(cardId);
                    info.balance -= cost;
                    out.writeUTF("Paid " + cost + " from card ID: " + cardId);
                } else {
                    out.writeUTF("Insufficient funds or card not found.");
                }
                break;
            case "balance":
                if (cards.containsKey(cardId)) {
                    UserCardInfo info = cards.get(cardId);
                    out.writeUTF("Current balance: " + info.balance);
                } else {
                    out.writeUTF("Card not found.");
                }
                break;
            default:
                out.writeUTF("Invalid action.");
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 12345;
        MetroCardServer server = new MetroCardServer(port);
        server.start();
    }
}
