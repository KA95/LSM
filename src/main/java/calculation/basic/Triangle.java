package calculation.basic;

import calculation.util.CalculationUtil;

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
