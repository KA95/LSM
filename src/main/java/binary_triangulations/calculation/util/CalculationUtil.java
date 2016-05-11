package binary_triangulations.calculation.util;

import binary_triangulations.calculation.model.Point2D;
import old.calculation.basic.Triangle2D;

public class CalculationUtil {
    public static final double EPS = 1e-7;

    public static boolean equals(double d1, double d2) {
        return Math.abs(d1 - d2) < EPS;
    }

    public static double dist2D(Point2D p1, Point2D p2) {
        double x1, x2, y1, y2;
        x1 = p1.x;
        x2 = p2.x;
        y1 = p1.y;
        y2 = p2.y;
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static double determinant2x2(double[][] m) {
        if (m.length != 2) throw new IllegalArgumentException();
        return m[0][0] * m[1][1] - m[0][1] * m[1][0];

    }

    public static boolean isPointInsideTriangle(Triangle2D triangle, Point2D point) {
        Triangle2D t1, t2, t3;
        t1 = new Triangle2D(point, triangle.getP1(), triangle.getP2());
        t2 = new Triangle2D(point, triangle.getP2(), triangle.getP3());
        t3 = new Triangle2D(point, triangle.getP1(), triangle.getP3());
        return equals(triangle.calculateSquare(), t1.calculateSquare() + t2.calculateSquare() + t3.calculateSquare());
    }

    public static boolean isPointStrictlyInsideTriangle(Triangle2D triangle, Point2D point) {
        Triangle2D t1, t2, t3;
        t1 = new Triangle2D(point, triangle.getP1(), triangle.getP2());
        t2 = new Triangle2D(point, triangle.getP2(), triangle.getP3());
        t3 = new Triangle2D(point, triangle.getP1(), triangle.getP3());
        double d1,d2,d3;
        d1 =  t1.calculateSquare();
        d2 =  t2.calculateSquare();
        d3 =  t3.calculateSquare();
        return  !equals(d1,0) && !equals(d2,0) && !equals(d3,0) && equals(triangle.calculateSquare(), d1 + d2 +d3);
    }
}
