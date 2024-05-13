import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

interface Task extends Serializable {
    Object execute();
}

class Server_Task1 {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(11000)) {
            System.out.println("Сервер запущений і очікує на з'єднання...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Клієнт підключений.");

                    Task task = (Task) input.readObject();
                    long startTime = System.nanoTime();
                    Object result = task.execute();
                    long endTime = System.nanoTime();
                    output.writeLong(endTime - startTime);
                    output.flush(); // Знову скидуємо буфер
                    output.writeObject(result);
                    output.flush(); // Скидуємо буфер


                    System.out.println("Завдання виконане і результат відправлено.");
                } catch (Exception e) {
                    System.err.println("Помилка при обробці запиту: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Не можу створити серверний сокет: " + e.getMessage());
        }
    }
}
