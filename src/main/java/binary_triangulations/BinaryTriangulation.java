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

    /**
     * Rebuild drawable shape.
     * Call it before drawing if triangulation was changed.
     */
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
    public DiscretePointDetailed activate1(DiscretePoint point) {
        System.out.println("point = " + point);
        point.normalize();
        List<DiscretePoint> parentPoints = getParents1(point.x, point.y, point.k);
        List<DiscretePointDetailed> parents = new ArrayList<DiscretePointDetailed>();
        for (DiscretePoint parentPoint : parentPoints) {
            System.out.println("parent : " + parentPoint);
            parentPoint.normalize();
            DiscretePointDetailed parentDetailed = activePoints.containsKey(parentPoint)
                    ? activePoints.get(parentPoint)
                    : activate2(parentPoint);
            parents.add(parentDetailed);
        }
        DiscretePointDetailed newPoint = new DiscretePointDetailed(point, parents);
        activePoints.put(newPoint.point, newPoint);
        return newPoint;
    }

    private DiscretePointDetailed activate2(DiscretePoint point) {
        point.normalize();
        List<DiscretePoint> parentPoints = getParents2(point.x, point.y, point.k);
        List<DiscretePointDetailed> parents = new ArrayList<DiscretePointDetailed>();
        for (DiscretePoint parentPoint : parentPoints) {
            System.out.println("parent : " + parentPoint);
            parentPoint.normalize();
            DiscretePointDetailed parentDetailed = activePoints.containsKey(parentPoint)
                    ? activePoints.get(parentPoint)
                    : activate1(parentPoint);
            parents.add(parentDetailed);
        }
        DiscretePointDetailed newPoint = new DiscretePointDetailed(point, parents);
        activePoints.put(newPoint.point, newPoint);
        return newPoint;
    }

    private List<DiscretePoint> getParents1(int x, int y, int k) {
        List<Integer> xList = getParentCoordinateList1(x, k);
        List<Integer> yList = getParentCoordinateList1(y, k);
        List<DiscretePoint> parents = new ArrayList<DiscretePoint>();
        for (int px : xList) {
            for (int py : yList) {
                parents.add(new DiscretePoint(px, py, k - 1));
            }
        }
        return parents;
    }

    private List<DiscretePoint> getParents2(int x, int y, int k) {
        List<Integer> xList = getParentCoordinateList2(x, k);
        List<Integer> yList = getParentCoordinateList2(y, k);
        List<DiscretePoint> parents = new ArrayList<DiscretePoint>();
        for (int px : xList) {
            parents.add(new DiscretePoint(px, y, k));
        }
        for (int py : yList) {
            parents.add(new DiscretePoint(x, py, k));
        }

        return parents;
    }

    private List<Integer> getParentCoordinateList1(int c, int k) {
        List<Integer> yList = new ArrayList<Integer>();

        int parentY = (c - 1) / 2;
        if (inBounds(parentY, k - 1)) {
            yList.add(parentY);
        }

        parentY = (c + 1) / 2;
        if (inBounds(parentY, k - 1)) {
            yList.add(parentY);
        }
        return yList;
    }

    private List<Integer> getParentCoordinateList2(int c, int k) {
        List<Integer> yList = new ArrayList<Integer>();

        int parentY = (c - 1);
        if (inBounds(parentY, k)) {
            yList.add(parentY);
        }

        parentY = (c + 1);
        if (inBounds(parentY, k)) {
            yList.add(parentY);
        }
        return yList;
    }

    private boolean inBounds(int c, int k) {
        return c >= 0 && c <= 1 << k;
    }

    @AllArgsConstructor
    private class DiscretePointDetailed {
        DiscretePoint point;

        List<DiscretePointDetailed> parents;

        public DiscretePointDetailed(int x, int y, int k, List<DiscretePointDetailed> parents) {
            this.point = new DiscretePoint(x, y, k);
            this.parents = parents;
        }

        public Coord3d getCoordinates() {
            double factor = 1 << point.k; //2^k
            return new Coord3d(point.x * (right - left) / factor, point.y * (top - bottom) / factor, 1);
        }
    }
}

