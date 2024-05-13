import java.net.DatagramPacket;
import java.net.DatagramSocket;

class UDPEchoServer {
    public static void main(String[] args) {
        int port = 9876; // Порт, на якому сервер слухає

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP ехо-сервер запущено на порту " + port);

            // Буфер для отримання вхідних даних
            byte[] receiveBuffer = new byte[1024];

            while (true) {
                // Створення DatagramPacket для отримання даних
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                // Отримання даних та адреси відправника
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Отримано: " + message);

                // Відправлення відповіді назад до клієнта
                socket.send(receivePacket);
            }
        } catch (Exception e) {
            System.out.println("Помилка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
