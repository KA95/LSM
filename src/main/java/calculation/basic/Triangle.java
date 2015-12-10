package calculation.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * Triangle class, p1 p2 - is always longest edge.
 */
public class Triangle {
    Point p1, p2 ,p3;

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getP3() {
        return p3;
    }

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle(Triangle2D tr) {
        this.p1 = new Point(tr.getP1());
        this.p2 = new Point(tr.getP2());
        this.p3 = new Point(tr.getP3());
    }

    public List<Point> getVerticesList() {
        List<Point> result = new ArrayList<Point>();
        result.add(p1);
        result.add(p2);
        result.add(p3);
        return result;
    }

}
