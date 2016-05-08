package binary_triangulations.calculation.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Point3D {
    private double x,y,z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point2D p) {
        this.x = p.getX();
        this.y = p.getY();
        this.z = 0;
    }



    public Point3D minus(Point3D p) {
        return new Point3D(x - p.getX(),y - p.getY(), z - p.getZ());
    }
}
