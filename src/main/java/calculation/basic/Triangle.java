package calculation.basic;

import calculation.util.CalculationUtil;

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

        rearrange();
    }

    public List<Triangle> split() {
        List<Triangle> result = new ArrayList<Triangle>();
        Point pm = new Point((p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2);
        result.add(new Triangle(p1,p3,pm));
        result.add(new Triangle(p2,p3,pm));
        return result;
    }

    public List<Point> getVerticesList() {
        List<Point> result = new ArrayList<Point>();
        result.add(p1);
        result.add(p2);
        result.add(p3);
        return result;
    }

    private void rearrange() {
        double d1,d2,d3;
        d1 = CalculationUtil.dist2D(p1, p2);
        d2 = CalculationUtil.dist2D(p1,p3);
        d3 = CalculationUtil.dist2D(p2,p3);
        if(d1<d2 || d1<d3) {
            Point buf;
            if (d2>d3) {
                buf = p2;
                p2 = p3;
                p3 = buf;
            } else {
                buf = p3;
                p3 = p1;
                p1 = buf;
            }
        }
    }
}
