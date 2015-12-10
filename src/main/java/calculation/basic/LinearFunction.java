package calculation.basic;

import calculation.util.CalculationUtil;

/**
 * Created by Lenovo on 10.12.2015.
 */
public class LinearFunction {
    double A, B, C, D;

    public LinearFunction(double a, double b, double c, double d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public LinearFunction(Point p1, Point p2, Point p3) {
        fillInfo(p1, p2, p3);
    }

    private void fillInfo(Point p1, Point p2, Point p3) {
        Point v, w;
        v = p2.minus(p1);
        w = p3.minus(p1);
        double[][] d1 = {{v.getX(), v.getY()}, {w.getX(), w.getY()}};
        double[][] d2 = {{v.getX(), v.getZ()}, {w.getX(), w.getZ()}};
        double[][] d3 = {{v.getY(), v.getZ()}, {w.getY(), w.getZ()}};
        A = CalculationUtil.determinant2x2(d3);
        B = -CalculationUtil.determinant2x2(d2);
        C = CalculationUtil.determinant2x2(d1);
        D = -(A * p1.getX() + B * p1.getY() + C * p1.getZ());
    }

    public LinearFunction(Triangle t) {
        fillInfo(t.getP1(), t.getP2(), t.getP3());
    }

    public double calculateZ(double x, double y) {
        return (A * x + B * y + D) / (-C);
    }

    @Override
    public String toString() {
        return String.format("a: %f\tb: %f\tc: %f\td: %f\t", A, B, C, D);
    }
}
