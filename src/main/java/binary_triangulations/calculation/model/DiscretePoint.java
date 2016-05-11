package binary_triangulations.calculation.model;

import binary_triangulations.exception.ConstructionException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class DiscretePoint {
    public int x;

    public int y;

    public int k;

    public DiscretePoint(int x, int y, int k) {
        this.x = x;
        this.y = y;
        this.k = k;
        normalize();
    }

    DiscretePoint(int x, int y, int k, boolean needNormalize) {
        this.x = x;
        this.y = y;
        this.k = k;
        if(needNormalize) {
            normalize();
        }
    }

    public void normalize() {
        while (k > 0 && ((x | y) & 1) == 0) { //x%2 == 0 and y%2 == 0
            x = x >> 1;
            y = y >> 1;
            k--;
        }
    }

    public DiscretePoint getNotNormalForm(int newK) {
        int difference = newK - k;
        if(difference < 0) {
            throw new ConstructionException("New k cannot be less than old. Point3D is " + this + ", new k is " + newK);
        }
        return new DiscretePoint(x << difference,
                y << difference,
                newK,
                false);
    }

    public static boolean angleGEthan180(DiscretePoint p1, DiscretePoint point, DiscretePoint p2) {
        int maxK = Math.max(p1.k, Math.max(p2.k, point.k));
        p1 = p1.getNotNormalForm(maxK);
        p2 = p2.getNotNormalForm(maxK);
        point = point.getNotNormalForm(maxK);
        int ax, ay, bx, by;
        ax = p1.x - point.x;
        ay = p1.y - point.y;
        bx = p2.x - point.x;
        by = p2.y - point.y;

        return ax * by - ay * bx <= 0;

    }
}
