package binary_triangulations;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiscretePoint {
    int x;

    int y;

    int k;

    @Override
    public String toString() {
        return "DiscretePoint{" +
                "x=" + x +
                ", y=" + y +
                ", k=" + k +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscretePoint point = (DiscretePoint) o;

        return x == point.x && y == point.y && k == point.k;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + k;
        return result;
    }

    public void normalize() {
        while (k > 0 && ((x | y) & 1) == 0) { //x%2 == 0 and y%2 == 0
            x = x >> 1;
            y = y >> 1;
            k--;
        }
    }
}
