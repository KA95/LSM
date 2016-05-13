package binary_triangulations.calculation.model.basic;

import binary_triangulations.calculation.util.CalculationUtil;

public class LinearFunction {
    double A, B, C, D;

    public LinearFunction(double a, double b, double c, double d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public LinearFunction(Point3D p1, Point3D p2, Point3D p3) {
        fillInfo(p1, p2, p3);
    }

    private void fillInfo(Point3D p1, Point3D p2, Point3D p3) {
        Point3D v, w;
        v = p2.minus(p1);
        w = p3.minus(p1);
        double[][] d1 = {{v.x, v.y}, {w.x, w.y}};
        double[][] d2 = {{v.x, v.z}, {w.x, w.z}};
        double[][] d3 = {{v.y, v.z}, {w.y, w.z}};
        A = CalculationUtil.determinant2x2(d3);
        B = -CalculationUtil.determinant2x2(d2);
        C = CalculationUtil.determinant2x2(d1);
        D = -(A * p1.x + B * p1.y + C * p1.z);
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
