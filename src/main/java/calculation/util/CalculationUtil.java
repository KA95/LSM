package calculation.util;

import calculation.basic.Point;

public class CalculationUtil {
    public static final double EPS = 1e-7;

    public static boolean equals (double d1, double d2) {
        return Math.abs(d1-d2)<EPS;
    }

    public static double dist2D(Point p1, Point p2) {
        double x1,x2,y1,y2;
        x1 = p1.getX();
        x2 = p2.getX();
        y1 = p1.getY();
        y2 = p2.getY();
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}
