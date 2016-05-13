package binary_triangulations.calculation.model.basic;

import binary_triangulations.calculation.util.CalculationUtil;

import java.util.ArrayList;
import java.util.List;

public class Triangle2D {
    private Point2D p1,p2,p3;

    public Point2D getP1() {
        return p1;
    }

    public void setP1(Point2D p1) {
        this.p1 = p1;
    }

    public Point2D getP2() {
        return p2;
    }

    public void setP2(Point2D p2) {
        this.p2 = p2;
    }

    public Point2D getP3() {
        return p3;
    }

    public void setP3(Point2D p3) {
        this.p3 = p3;
    }

    public Triangle2D(Point2D p1, Point2D p2, Point2D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        rearrange();
    }

    public Triangle2D(Triangle tr) {
        this.p1 = new Point2D(tr.getP1());
        this.p2 = new Point2D(tr.getP2());
        this.p3 = new Point2D(tr.getP3());
        rearrange();
    }

    public List<Triangle2D> split() {
        List<Triangle2D> result = new ArrayList<>();
        Point2D pm = new Point2D((p1.x + p2.x)/2, (p1.y + p2.y)/2);
        result.add(new Triangle2D(p1,p3,pm));
        result.add(new Triangle2D(p2,p3,pm));
        return result;
    }

    private void rearrange() {
        double d1,d2,d3;
        d1 = CalculationUtil.dist2D(p1, p2);
        d2 = CalculationUtil.dist2D(p1,p3);
        d3 = CalculationUtil.dist2D(p2,p3);
        if(d1<d2 || d1<d3) {
            Point2D buf;
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

    public double calculateSquare() {
        double d1,d2,d3;
        d1 = CalculationUtil.dist2D(p1,p2);
        d2 = CalculationUtil.dist2D(p1,p3);
        d3 = CalculationUtil.dist2D(p2,p3);
        double p = (d1+d2+d3)/2;
        return Math.sqrt(p*(p-d1)*(p-d2)*(p-d3));
    }

    public List<Point2D> getVerticesList() {
        List<Point2D> result = new ArrayList<>();
        result.add(p1);
        result.add(p2);
        result.add(p3);
        return result;
    }

}
