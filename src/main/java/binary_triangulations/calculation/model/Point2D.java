package binary_triangulations.calculation.model;

import binary_triangulations.calculation.util.CalculationUtil;

public class Point2D implements Comparable<Point2D> {
    double x,y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point3D p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public int compareTo(Point2D o) {
        if(!CalculationUtil.equals(x, o.getX()))
            return Double.compare(x,o.getX());
        if(!CalculationUtil.equals(y,o.getY()))
            return Double.compare(y,o.getY());
        return Double.compare(x,o.getX());
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Point2D) {
            Point2D p = (Point2D) o;
            return CalculationUtil.equals(p.getX(), x) &&
                    CalculationUtil.equals(p.getY(), y);
        }
        return false;
    }

    @Override
    public String toString() {
        String result = super.toString() + "\n";
        result += String.format("x : %f\n",x);
        result += String.format("y : %f\n",y);
        return result;
    }

}
