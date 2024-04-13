import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
public class Zavd2 {
    public static void inspectObject(Object object) {
        System.out.println("Стан об'єкту:");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                System.out.println(field.getType().getName() + " " + field.getName() + " = " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nСписок публічних методів:");
        Method[] methods = object.getClass().getDeclaredMethods();
        int count = 1;
        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                System.out.println(count + "). " + method);
                count++;
            }
        }
    }
    public static void callMethod(Object object, int methodIndex) {
        Method[] methods = object.getClass().getDeclaredMethods();
        int count = 1;
        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                if (count == methodIndex) {
                    try {
                        Object result = method.invoke(object);
                        System.out.println("Результат виклику методу: " + result);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                count++;
            }
        }
    }
    public static void main(String[] args) {
        Check object = new Check(7.0, 12.0);
        inspectObject(object);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Виберіть опцію:");
            System.out.println("1. Викликати метод об'єкта Check");
            System.out.println("2. Вийти з програми");
            System.out.print("Ваш вибір: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введіть індекс методу для виклику: ");
                    int methodIndex = scanner.nextInt();
                    callMethod(object, methodIndex);
                    break;
                case 2:
                    System.out.println("Вихід з програми...");
                    scanner.close();
                    return;
            }
        }
    }
    static class Check {
        private double x;
        private double y;
        public Check(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public double dist() {
            return Math.sqrt(x * x + y * y);
        }
        public String setRandomData() {
            x = Math.random() * 10;
            y = Math.random() * 10;
            return "x = " + x + ", y = " + y;
        }
        @Override
        public String toString() {
            return "x = " + x + ", y = " + y;
        }
    }
}