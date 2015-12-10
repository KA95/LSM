package calculation.basic;

/**
 * Created by Lenovo on 10.12.2015.
 */
public class LinearFunction {
    double A,B,C,D;

    public LinearFunction(double a, double b, double c, double d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public LinearFunction(Point p1, Point p2, Point p3) {
        //TODO implement
    }

    public double calculateZ(double x, double y) {
        return (A*x+B*y+D)/(-C);
    }
}
