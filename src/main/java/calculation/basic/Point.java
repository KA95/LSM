package calculation.basic;

import calculation.util.CalculationUtil;

public class Point {
    private double x,y,z;

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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Point) {
            Point p = (Point) o;
            return CalculationUtil.equals(p.getX(), x) &&
                    CalculationUtil.equals(p.getY(), y) &&
                    CalculationUtil.equals(p.getZ(), z);
        }
        return false;
    }

    @Override
    public String toString() {
        String result = super.toString() + "\n";
        result += String.format("x : %f\n",x);
        result += String.format("y : %f\n",y);
        result += String.format("z : %f\n",z);
        return result;
    }
}
