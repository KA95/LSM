package calculation.advanced;

import calculation.basic.LinearFunction;
import calculation.basic.Point2D;
import calculation.basic.Triangle;
import calculation.basic.Triangle2D;
import calculation.util.CalculationUtil;

import java.util.ArrayList;
import java.util.List;

public class PyramidalFunction {
    Point2D center;
    List<Triangle> parts;

    public List<Triangle> getParts() {
        return parts;
    }

    public void setParts(List<Triangle> parts) {
        this.parts = parts;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    PyramidalFunction(Point2D center, Triangle part) {
        this.center = center;
        parts = new ArrayList<Triangle>();
        parts.add(part);
    }

    public void addPart(Triangle tr) {
        parts.add(tr);
    }

    public double getValue(double x, double y) {
        for(Triangle tr : parts) {
            Triangle2D t2d = new Triangle2D(tr);
            if(CalculationUtil.isPointStrictlyInsideTriangle(t2d, new Point2D(x,y))) {
                LinearFunction lf = new LinearFunction(tr);
                return lf.calculateZ(x,y);
            }
        }
        return 0;
    }
}
