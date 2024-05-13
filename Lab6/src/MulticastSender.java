import java.io.*;
import java.net.*;

public class MulticastSender {
    private InetAddress group;
    private int port;

    public MulticastSender(String multicastAddress, int port) throws UnknownHostException {
        this.group = InetAddress.getByName(multicastAddress);
        this.port = port;
    }

    public void sendMessage(String message) throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
            System.out.println("Message sent: " + message);
        }
    }

    public static void main(String[] args) throws IOException {
        MulticastSender sender = new MulticastSender("230.0.0.0", 4446);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while ((message = reader.readLine()) != null && !message.isEmpty()) {
            sender.sendMessage(message);
        }
    }
}
