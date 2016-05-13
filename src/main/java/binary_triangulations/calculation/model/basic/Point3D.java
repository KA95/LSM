package binary_triangulations.calculation.model.basic;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Point3D {
    public double x,y,z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point2D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = 0;
    }

    public Point3D minus(Point3D p) {
        return new Point3D(x - p.x,y - p.y, z - p.z);
    }
}
