import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
interface Evaluatable {
    double eval(double x);
}
class Function1 implements Evaluatable {
    @Override
    public double eval(double x) {
        return Math.exp(-Math.abs(2.5 * x)) * Math.sin(x);
    }
}
class Function2 implements Evaluatable {
    @Override
    public double eval(double x) {
        return x * x;
    }
}
class ProfilingHandler implements InvocationHandler {
    private final Object target;
    ProfilingHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.nanoTime();
        Object result = method.invoke(target, args);
        long endTime = System.nanoTime();
        System.out.println(method.getName() + " took " + (endTime - startTime) + " ns");
        return result;
    }
}
class TracingHandler implements InvocationHandler {
    private final Object target;

    TracingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print("[" + target.getClass().getSimpleName() + "]." + method.getName());
        if (args != null && args.length > 0) {
            System.out.print("(");
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                if (i < args.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print(")");
        }
        Object result = method.invoke(target, args);
        System.out.println(" = " + result);
        return result;
    }
}
public class Zavd5 {
    public static void main(String[] args) {
        Evaluatable function1 = new Function1();
        Evaluatable function2 = new Function2();

        Evaluatable profilingProxy1 = (Evaluatable) Proxy.newProxyInstance(
                Evaluatable.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new ProfilingHandler(function1));

        Evaluatable profilingProxy2 = (Evaluatable) Proxy.newProxyInstance(
                Evaluatable.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new ProfilingHandler(function2));

        Evaluatable tracingProxy1 = (Evaluatable) Proxy.newProxyInstance(
                Evaluatable.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new TracingHandler(function1));

        Evaluatable tracingProxy2 = (Evaluatable) Proxy.newProxyInstance(
                Evaluatable.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new TracingHandler(function2));

        double x = 88.0;

        System.out.println("F1: " + profilingProxy1.eval(x));
        System.out.println("F2: " + profilingProxy2.eval(x));

        System.out.println("F1: " + tracingProxy1.eval(x));
        System.out.println("F2: " + tracingProxy2.eval(x));
    }
}