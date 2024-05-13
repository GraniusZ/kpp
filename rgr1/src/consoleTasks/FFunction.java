package consoleTasks;

import java.awt.*;
import java.util.ArrayList;

public class FFunction implements Evaluatable{
    private double a;
    private double step;

    public FFunction(double a, double step) {
        this.a = a;
        this.step = step;
    }
    public FFunction() {
        this(1.0, 0.05);
    }
    public double getA() {
        return a;
    }
    public void setA(double a) {
        this.a = a;
    }
    @Override
    public double evalf(double x) {
        return Math.exp(-a*x*x)*Math.sin(x);
    }

    public double evalfDef(double x){
        return (evalf(x+step)-evalf(x-step))/(2*step);
    }


    public static void main(String[] args) {
        ArrayList points1 = new ArrayList<Point2D>();
        ArrayList pointsDef1 = new ArrayList<Point2D>();

        ArrayList points2 = new ArrayList<Point2D>();
        ArrayList pointsDef2 = new ArrayList<Point2D>();

        System.out.println("Перевірка класу FFunction");
        FFunction fun1 = new FFunction();

        java.util.Scanner in = new java.util.Scanner(System.in);
        System.out.print("a: ");
        double a = in.nextDouble();

        System.out.print("xStep: ");
        double xStep = in.nextDouble();

        while(xStep <= 0){
            System.out.println("Помилка. Спробуйте ще раз: ");
            xStep = in.nextDouble();
        }

        FFunction fun2 = new FFunction(a,xStep);

        System.out.print("xBeg: ");
        double xBeg = in.nextDouble();
        System.out.print("xEnd: ");
        double xEnd = in.nextDouble();

        while(xBeg >= xEnd){
            System.out.println("Помилка. Спробуйте ще раз: ");
            xEnd = in.nextDouble();
        }

        System.out.println("Параметр a: " + fun1.getA());
        for (double x = xBeg; x <= xEnd; x += xStep){
            System.out.printf("x: %6.4f\tf: %6.9f\tdef: %6.9f\n", x, fun1.evalf(x),fun1.evalfDef(x));
            Point2D point = new Point2D(x, fun1.evalf(x));
            Point2D defPoint = new Point2D(x, fun1.evalfDef(x));
            points1.add(point);
            pointsDef1.add(defPoint);
        }

        System.out.println("Параметр a: " + fun2.getA());
        for (double x = xBeg; x <= xEnd; x += xStep){
            System.out.printf("x: %6.4f\tf: %6.9f\tdef: %6.9f\n", x, fun2.evalf(x),fun2.evalfDef(x));
            Point2D point = new Point2D(x, fun2.evalf(x));
            Point2D defPoint = new Point2D(x, fun2.evalfDef(x));
            points2.add(point);
            pointsDef2.add(defPoint);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PlotFrame frame = new PlotFrame(points1, pointsDef1, "y = exp(-x^2)*sin(x)");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PlotFrame frame = new PlotFrame(points2,pointsDef2, "y = exp(-a*x^2)*sin(x)");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
