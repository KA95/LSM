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

    public List<Triangle> getParts() {
        return parts;
    }

    public PyramidalFunction(Point2D center, List<Triangle> parts, DiscretePoint discreteCenter) {
        this.center = center;
        this.parts = parts;
        this.discreteCenter = discreteCenter;
    }

    public double getValue(double x, double y) {
        for(Triangle tr : parts) {
            Triangle2D t2d = new Triangle2D(tr);
            if(CalculationUtil.isPointInsideTriangle(t2d, new Point2D(x,y))) {
                LinearFunction lf = new LinearFunction(tr);
                return lf.calculateZ(x,y);
            }
        }
        return 0;
    }
}
