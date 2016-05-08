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
}
