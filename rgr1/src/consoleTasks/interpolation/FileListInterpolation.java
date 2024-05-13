package consoleTasks.interpolation;
import com.sun.source.tree.Tree;
import consoleTasks.PlotFrame;
import consoleTasks.Point2D;

import java.awt.*;
import java.io.*;
import java.util.*;

public class FileListInterpolation extends  ListInterpolation{
    private TreeMap<Double, Double> storage = null;
    private TreeSet<Point2D> storage2 = null;

    public FileListInterpolation() {
        super();
    }
    public FileListInterpolation(TreeMap<Double, Double> storage) {
        this.storage = storage;
    }
    public FileListInterpolation(TreeSet<Point2D> storage2) {
        this.storage2 = storage2;
    }
    public void readFromFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s = in.readLine(); // чтение строки с заголовками столбцов
        clear();
        while ((s = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s);
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            addPoint(new Point2D(x, y));
        }
        in.close();
    }
    public void writeToFile(String fileName) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        out.printf("%9s%25s\n", "x", "y");
        for (int i = 0; i < numPoints(); i++) {
            out.println(getPoint(i).getX() + "\t" + getPoint(i).getY());
        }
        out.close();
    }

    public double evalfDef(double x){
        return (Math.sin(x+0.1)-Math.sin(x-0.1))/(2*0.1);
    }


    public void addPointMap(Point2D pt) {
        storage.put(pt.getX(), pt.getY());

    }

    public void addPointSet(Point2D pt){
        storage2.add(pt);
    }


    public TreeMap<Double, Double> getStorageMap() {
        return storage;
    }

    public TreeSet<Point2D> getStorageSet() {
        return storage2;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        FileListInterpolation fun = new FileListInterpolation();
        FileListInterpolation fun2 = new FileListInterpolation(new TreeMap<Double, Double>());
        FileListInterpolation fun3 = new FileListInterpolation(new TreeSet<Point2D>());

        int num;
        double x;
        java.util.Scanner in = new java.util.Scanner(System.in);
        do {
            System.out.print("Кількість точок: ");
            num = in.nextInt();
        } while (num <= 0);

        for (int i = 0; i < num; i++) {
            x = 1.0 + (5.0 - 1.0)*Math.random();
            fun.addPoint(new Point2D(x, Math.sin(x)));
            fun2.addPointMap(new Point2D(x, Math.sin(x)));
            fun3.addPointSet(new Point2D(x, Math.sin(x)));

        }

        System.out.println("Інтерполяція по: " + fun.numPoints() + " точкам");

        System.out.println("Непосортований набір: ");
        for (int i = 0; i < fun.numPoints(); i++)
            System.out.println("Точка " + (i+1) + ": " + fun.getPoint(i));
        fun.sort();
        System.out.println("Посоротований набір: ");
        for (int i = 0; i < fun.numPoints(); i++)
            System.out.println("Точка " + (i+1) + ": " + fun.getPoint(i));

        System.out.println("Мінімальне значення x: " + fun.getPoint(0).getX());
        System.out.println("Максимальне значення x: " +
                fun.getPoint(fun.numPoints()-1).getX());


        System.out.println("\nTreeSet\n");

        for (Point2D value : fun3.getStorageSet()){
            System.out.print(value.getX() + ", "+ value.getY());
        System.out.println();
          }

        System.out.println("\nTreeMap\n");

        Set set = fun2.getStorageMap().entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext()) {
            Map.Entry me = (Map.Entry)iterator.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }


        System.out.println("Зберігаємо у файл");
        try {
            fun.writeToFile("data.dat");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Зчитуємо з файлу");
        fun.clear();
        try {
            fun.readFromFile("data.dat");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Дані із файлу: ");
        fun.sort();
        for (int i = 0; i < fun.numPoints(); i++)
            System.out.println("Точка " + (i+1) + ": " + fun.getPoint(i));
        System.out.println("Мінімальне значення x: " + fun.getPoint(0).getX());
        System.out.println("Максимальне значення x: " +
                fun.getPoint(fun.numPoints()-1).getX());
        x = 0.5*(fun.getPoint(0).getX() +
                fun.getPoint(fun.numPoints()-1).getX());
        System.out.println("Значення інтерполяції fun(" + x + ") = " +
                fun.evalf(x));
        System.out.println("Точне значення sin(" + x + ") = " + Math.sin(x));
        System.out.println("Абсолютна помилка = " +
                Math.abs(fun.evalf(x)-Math.sin(x)));
        System.out.println("Готуємо данні для розрахунку");

        fun.clear();

        ArrayList <Point2D> points = new ArrayList<>();
        ArrayList <Point2D> pointsDef = new ArrayList<>();

        for (x = 1.0; x <= 7.0; x += 0.1) {
            fun.addPoint(new Point2D(x, Math.sin(x)));
            points.add(new Point2D(x, fun.evalf(x)));
            pointsDef.add(new Point2D(x, fun.evalfDef(x)));
        }
        try {
            fun.writeToFile("TblFunc.dat");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PlotFrame frame = new PlotFrame(points,pointsDef, "sin(x)");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
