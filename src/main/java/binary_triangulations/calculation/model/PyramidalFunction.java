package binary_triangulations.calculation.model;

import binary_triangulations.calculation.model.basic.*;
import lombok.Getter;
import lombok.Setter;
import binary_triangulations.calculation.util.CalculationUtil;

import java.util.List;

@Getter
@Setter
public class PyramidalFunction {
    DiscretePoint discreteCenter;
    Point2D center;
    List<Triangle> parts;

    private double xmax;
    private double xmin;
    private double ymax;
    private double ymin;

    public List<Triangle> getParts() {
        return parts;
    }

    public PyramidalFunction(Point2D center, List<Triangle> parts, DiscretePoint discreteCenter) {
        this.center = center;
        this.parts = parts;
        this.discreteCenter = discreteCenter;
        for (Triangle t : parts) {
            for (Point3D p : t.getVerticesList()) {
                xmax = Math.max(xmax, p.x);
                ymax = Math.max(ymax, p.y);
                xmin = Math.min(xmin, p.x);
                ymin = Math.min(ymin, p.y);
            }
        }
    }

    public double getValue(double x, double y) {
        if (!(xmin < x && x < xmax && ymin < y && y < ymax))
            return 0;

        for (Triangle tr : parts) {
            Triangle2D t2d = new Triangle2D(tr);
            if (CalculationUtil.isPointInsideTriangle(t2d, new Point2D(x, y))) {
                LinearFunction lf = new LinearFunction(tr);
                return lf.calculateZ(x, y);
            }
        }
        return 0;
    }
}
