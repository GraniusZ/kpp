import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UDPEchoClient {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1"; // IP адреса сервера
        int serverPort = 9876; // Порт сервера
        String message = "Привіт, це тестове повідомлення"; // Повідомлення для відправлення

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(serverIP);

            // Перетворення рядка повідомлення в масив байтів
            byte[] sendData = message.getBytes();

            // Створення DatagramPacket для відправлення даних
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, serverPort);
            socket.send(sendPacket);

            // Буфер для отримання відповіді
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            // Отримання відповіді від сервера
            socket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Відповідь від сервера: " + receivedMessage);
        } catch (Exception e) {
            System.out.println("Помилка клієнта: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
