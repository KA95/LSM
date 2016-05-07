package binary_triangulations;

import drawing.util.DrawingUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class BinaryTriangulation {

    private double left;
    private double right;
    private double top;
    private double bottom;

    private int gridPower;

    Shape shape;

    //active points that stored in normalized(!!!) form
    private Map<DiscretePoint, DiscretePointDetailed> activePoints = new HashMap<DiscretePoint, DiscretePointDetailed>();

    public BinaryTriangulation(double left, double bottom, double right, double top) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

        initialize();
    }

    /**
     * Initialize basic triangulation with four vertices with regular grid PSI_0(1x1)
     * and generate drawable shape.
     */
    private void initialize() {
        DiscretePointDetailed p1 = new DiscretePointDetailed(1, 0, 0, null);
        DiscretePointDetailed p2 = new DiscretePointDetailed(0, 1, 0, null);
        DiscretePointDetailed p3 = new DiscretePointDetailed(1, 1, 0, Arrays.asList(p1, p2));
        DiscretePointDetailed p4 = new DiscretePointDetailed(0, 0, 0, Arrays.asList(p1, p2, p3));
        activePoints.put(p1.point, p1);
        activePoints.put(p2.point, p2);
        activePoints.put(p3.point, p3);
        activePoints.put(p4.point, p4);
        rebuildShape();
    }

    public void rebuildShape() {
        List<Polygon> lines = new ArrayList<Polygon>();
        for (DiscretePointDetailed point : activePoints.values()) {
            if (point.parents != null) {
                for (DiscretePointDetailed parent : point.parents) {
                    lines.add(DrawingUtil.createLine(point.getCoordinates(), parent.getCoordinates()));
                }
            }
        }
        shape = new Shape(lines);
    }

    /**
     * Refine grid cell containing point
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void refine(double x, double y) {
        //should be reduced to refine(x,y,k)
    }

    //todo: make private
    public void refine(int x, int y, int k) {
        // todo: all

        rebuildShape();
    }

//    private void activate

    @AllArgsConstructor
    private class DiscretePoint {
        int x;

        int y;

        //power of grid
        int k;

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

    @AllArgsConstructor
    private class DiscretePointDetailed {
        DiscretePoint point;

        List<DiscretePointDetailed> parents;

        public DiscretePointDetailed(int x, int y, int k, List<DiscretePointDetailed> parents) {
            this.point = new DiscretePoint(x,y,k);
            this.parents = parents;
        }

        public Coord3d getCoordinates() {
            double factor = 1 << point.k; //2^k
            return new Coord3d(point.x * (right - left) / factor, point.y * (top - bottom) / factor, 1);
        }
    }
}
