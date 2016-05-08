package old.calculation.basic;

import binary_triangulations.calculation.model.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Triangle class, p1 p2 - is always longest edge.
 */
public class Triangle {
    Point3D p1, p2 ,p3;

    public Point3D getP1() {
        return p1;
    }

    public Point3D getP2() {
        return p2;
    }

    public Point3D getP3() {
        return p3;
    }

    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle(Triangle2D tr) {
        this.p1 = new Point3D(tr.getP1());
        this.p2 = new Point3D(tr.getP2());
        this.p3 = new Point3D(tr.getP3());
    }

    public List<Point3D> getVerticesList() {
        List<Point3D> result = new ArrayList<Point3D>();
        result.add(p1);
        result.add(p2);
        result.add(p3);
        return result;
    }

}
