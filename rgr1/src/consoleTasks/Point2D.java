package consoleTasks;

public class Point2D extends Point implements Comparable<Point2D>{
    public Point2D(double x, double y) {
        super(2);
        setCoord(1, x); setCoord(2, y);
    }
    public Point2D() {
        this(0, 0);
    }
    public double getX() {
        return getCoords(1);
    }
    public void setX(double x) {
        setCoord(1, x);
    }
    public double getY() {
        return getCoords(2);
    }
    public void setY(double y) {
        setCoord(2, y);
    }

    @Override
    public int compareTo(Point2D o) {
        if(o.getX() > this.getX()){
            return -1;
        }
        if(o.getX() == this.getX()){
            return 0;
        }

        return 1;
    }

    public static void main(String[] args) {
        java.util.List<Point2D> data = new java.util.ArrayList<Point2D>();
        int num;
        double x;
        java.util.Scanner in = new java.util.Scanner(System.in);
        do {
            System.out.print("Кількість точок: ");
            num = in.nextInt();
        } while (num <= 0);
        for (int i = 0; i < num; i++)
        {
            x = 1.0 + (5.0 - 1.0)*Math.random();
            data.add(new Point2D(x, Math.sin(x)));
        }
        System.out.println("Непосортованні дані: ");
        for (Point2D pt : data)
            System.out.println(pt);
        System.out.println("\nПосортованні дані: ");
        java.util.Collections.sort(data);
        for (Point2D pt : data)
            System.out.println("x = " + pt.getX() + "\ty = " + pt.getY());
    }

}
