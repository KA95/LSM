package binary_triangulations.calculation.model.basic;

import binary_triangulations.calculation.util.CalculationUtil;

public class Point2D implements Comparable<Point2D> {
    public double x,y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
    }

    public int compareTo(Point2D o) {
        if(!CalculationUtil.equals(x, o.x))
            return Double.compare(x,o.x);
        if(!CalculationUtil.equals(y,o.y))
            return Double.compare(y,o.y);
        return Double.compare(x,o.x);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Point2D) {
            Point2D p = (Point2D) o;
            return CalculationUtil.equals(p.x, x) &&
                    CalculationUtil.equals(p.y, y);
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
