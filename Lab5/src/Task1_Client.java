import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

class Client_Task1 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 11000);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            Task factorialTask = new FactorialTask(20);
            output.writeObject(factorialTask);
            output.flush(); // Скидуємо буфер, щоб відправити об'єкт
            long timeTaken = input.readLong();
            Object result = input.readObject();


            System.out.println("Результат: " + result);
            System.out.println("Час виконання (нс): " + timeTaken);
        } catch (Exception e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }

    private static class FactorialTask implements Task {
        private final int number;

        public FactorialTask(int number) {
            this.number = number;
        }

        public Object execute() {
            return factorial(number);
        }

        private long factorial(int n) {
            long result = 1;
            for (int i = 1; i <= n; i++) {
                result *= i;
            }
            return result;
        }
    }
}
