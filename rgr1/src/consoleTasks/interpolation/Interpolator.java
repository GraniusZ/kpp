package consoleTasks.interpolation;

import consoleTasks.Evaluatable;
import consoleTasks.Point2D;

public abstract class Interpolator implements Evaluatable {
    abstract public void clear();
    abstract public int numPoints();
    abstract public void addPoint(Point2D pt);
    abstract public Point2D getPoint(int i);
    abstract public void setPoint(int i, Point2D pt);
    abstract public void removeLastPoint();
    abstract public void sort();


    @Override
    public double evalf(double x) {
        double res = 0.0;
        int numData = numPoints();
        double numer, denom;
        for (int k = 0; k < numData; k++) {
            numer = 1.0;
            denom = 1.0;
            for (int j = 0; j < numData; j++) {
                if (j != k) {
                    numer = numer * (x - getPoint(j).getX());
                    denom = denom * (getPoint(k).getX() - getPoint(j).getX());
                }
            }
            res = res + getPoint(k).getY()*numer/denom;
        }
        return res;
    }
}

