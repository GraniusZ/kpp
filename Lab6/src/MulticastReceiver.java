import java.io.*;
import java.net.*;

public class MulticastReceiver extends Thread {
    private InetAddress group;
    private int port;

    public MulticastReceiver(String multicastAddress, int port) throws IOException {
        this.group = InetAddress.getByName(multicastAddress);
        this.port = port;
    }

    public void run() {
        try (MulticastSocket socket = new MulticastSocket(port)) {
            socket.joinGroup(group);
            byte[] buffer = new byte[1000];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());

                // Видалення ідентифікатора користувача
                String messageContent = received.substring(received.indexOf(':') + 1);

                System.out.println("Received: " + messageContent);
            }
        } catch (IOException e) {
            System.out.println("Socket closed!");
        }
    }

    public static void main(String[] args) throws IOException {
        MulticastReceiver receiver = new MulticastReceiver("230.0.0.0", 4446);
        receiver.start();
    }
}
