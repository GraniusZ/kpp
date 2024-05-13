import java.io.IOException;
import java.net.*;

class UDPClient {
    private DatagramSocket socket;
    private InetAddress address;

    public UDPClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public void sendAndReceive() throws IOException {
        String msg = "Register";
        byte[] sendData = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9876);
        socket.send(sendPacket);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        System.out.println("Received: " + new String(receivePacket.getData(), 0, receivePacket.getLength()));
    }

    public static void main(String[] args) throws IOException {
        UDPClient client = new UDPClient();
        client.sendAndReceive();
    }
}
