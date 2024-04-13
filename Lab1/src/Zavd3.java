import java.lang.reflect.Method;
class FunctionNotFoundException extends Exception {
    public FunctionNotFoundException(String message) {
        super(message);
    }
}
public class Zavd3 {
    public static void main(String[] args) {
        TestClass testObject = new TestClass();
        try {
            Object result1 = callMethod(testObject, "evaluate", new Object[]{7.0});
            System.out.println("Результат виклику методу: " + result1);

            Object result2 = callMethod(testObject, "evaluatehard", new Object[]{2.0, 4});
            System.out.println("Результат виклику методу: " + result2);

            Object result3 = callMethod(testObject, "evaluatehard", new Object[]{9, 9});
            System.out.println("Результат виклику методу: " + result3);
        } catch (FunctionNotFoundException  e) {
            System.out.println("Помилка виклику методу: " + e.getMessage());
        }
    }
    public static Object callMethod(Object object, String methodName, Object[] parameters) throws FunctionNotFoundException {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
            if (parameterTypes[i] == Double.class) {
                parameterTypes[i] = double.class;
            } else if (parameterTypes[i] == Integer.class) {
                parameterTypes[i] = int.class;
            }
        }
        try {
            Method method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
            return method.invoke(object, parameters);
        } catch (NoSuchMethodException e) {
            throw new FunctionNotFoundException("Метод " + methodName + " не знайдено із заданими параметрами");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
class TestClass {
    public double evaluate(double a) {
        return Math.sin(a);
    }
    public double evaluatehard(double a, int x) {
        return Math.exp(-Math.abs(a) * x) * Math.sin(x);
    }
}