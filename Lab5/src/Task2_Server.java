import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

class UDPServer {
    private DatagramSocket socket;
    private Set<String> registeredClients;

    public UDPServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        registeredClients = new HashSet<>();
    }

    public void listen() throws IOException {
        byte[] receiveBuffer = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            String clientData = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String clientAddress = receivePacket.getAddress().toString() + ":" + receivePacket.getPort();

            registeredClients.add(clientAddress);
            String response = String.join(", ", registeredClients);

            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(sendPacket);
        }
    }

    public static void main(String[] args) throws IOException {
        UDPServer server = new UDPServer(9876);
        server.listen();
    }
}
