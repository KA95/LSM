package binary_triangulations.calculation;

import binary_triangulations.calculation.model.DiscretePoint;
import binary_triangulations.exception.ConstructionException;
import old.drawing.util.DrawingUtil;
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

    private static final double EPS = 1.0e-9;

    private double left;
    private double right;
    private double top;
    private double bottom;

    private int gridDegree = 0; //grid is (2^gridDegree x 2^gridDegree) regular cell

    Shape shape;

    private Map<DiscretePoint, DiscretePointDetailed> activePoints = new HashMap<DiscretePoint, DiscretePointDetailed>();

    @AllArgsConstructor
    public class DiscretePointDetailed {
        DiscretePoint point;

        ArrayList<DiscretePointDetailed> parents;

        public DiscretePointDetailed(int x, int y, int k, ArrayList<DiscretePointDetailed> parents) {
            this.point = new DiscretePoint(x, y, k);
            this.parents = parents;
        }

        public Coord3d getCoordinates() {
            double factor = 1 << point.k; //2^k
            return new Coord3d(point.x * (right - left) / factor, point.y * (top - bottom) / factor, 1);
        }
    }

    public BinaryTriangulation(double left, double bottom, double right, double top) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

        initialize();
    }

    /**
     * Initialize basic triangulation with four vertices with regular grid PSI_1(2x2)
     * and generate drawable shape.
     */
    private void initialize() {
        DiscretePointDetailed p1 = new DiscretePointDetailed(1, 0, 0, null);
        DiscretePointDetailed p2 = new DiscretePointDetailed(0, 1, 0, null);
        ArrayList<DiscretePointDetailed> list1 = new ArrayList<>(Arrays.asList(p1, p2));
        DiscretePointDetailed p3 = new DiscretePointDetailed(1, 1, 0, list1);
        ArrayList<DiscretePointDetailed> list2 = new ArrayList<>(Arrays.asList(p1, p2, p3));
        DiscretePointDetailed p4 = new DiscretePointDetailed(0, 0, 0, list2);
        activePoints.put(p1.point, p1);
        activePoints.put(p2.point, p2);
        activePoints.put(p3.point, p3);
        activePoints.put(p4.point, p4);
        activate(new DiscretePoint(1, 1, 1), ActivationType.FIRST);
        gridDegree = 1;
        rebuildShape();
    }

    /**
     * Rebuild drawable shape.
     * Call it before drawing if triangulation was changed.
     */
    public void rebuildShape() {
        List<Polygon> lines = new ArrayList<Polygon>();
        int v = 0;
        int e = 0;
        for (DiscretePointDetailed point : activePoints.values()) {
            v++;
            if (point.parents != null) {
                for (DiscretePointDetailed parent : point.parents) {
                    e++;
                    lines.add(DrawingUtil.createLine(point.getCoordinates(), parent.getCoordinates()));
                }
            }
        }
        shape = new Shape(lines);
        System.out.println("e = " + e);
        System.out.println("v = " + v);
    }

    public void degreeUp() {
        gridDegree++;
    }

    /**
     * Refine grid cell containing point
     *
     * @param realX coordinate
     * @param realY coordinate
     */
    public void refine(double realX, double realY) {
        List<Integer> xList = getDiscreteCoordinates(realX, left, right);
        List<Integer> yList = getDiscreteCoordinates(realY, bottom, top);
        for (int x : xList) {
            for (int y : yList) {
                activate(new DiscretePoint(x, y, gridDegree + 1), ActivationType.FIRST);
            }
        }
    }


    private List<Integer> getDiscreteCoordinates(double realC, double min, double max) {

        List<Integer> cList = new ArrayList<>();
        double factor = 1 << gridDegree;

        double cellSize = (max - min) / factor;
        double dCoordinate = realC / cellSize;

        int c1 = (int) Math.floor(dCoordinate + EPS);
        int c2 = (int) Math.floor(dCoordinate - EPS);

        cList.add(c1 * 2 + 1);
        if (c1 != c2) {
            cList.add(c2 * 2 + 1);
        }

        return cList;
    }

    /**
     * Activate points recursively with saving triangulation correctness.
     *
     * @param point point to activate
     * @param type  activation type
     * @return new point of triangulation
     */
    private DiscretePointDetailed activate(DiscretePoint point, ActivationType type) {
        List<DiscretePoint> parentPoints;
        if (type == ActivationType.FIRST) {
            parentPoints = getParents1(point.x, point.y, point.k);
        } else {
            parentPoints = getParents2(point.x, point.y, point.k);
        }
        checkParents(parentPoints, point);
        ArrayList<DiscretePointDetailed> parents = new ArrayList<>();
        for (DiscretePoint parentPoint : parentPoints) {
            DiscretePointDetailed parentDetailed =
                    activePoints.containsKey(parentPoint)
                            ? activePoints.get(parentPoint)
                            : (type == ActivationType.FIRST
                            ? activate(parentPoint, ActivationType.SECOND)
                            : activate(parentPoint, ActivationType.FIRST)
                    );
            parents.add(parentDetailed);
        }
        cleanupParentEdges(parents, point);
        DiscretePointDetailed newPoint = new DiscretePointDetailed(point, parents);
        activePoints.put(newPoint.point, newPoint);
        return newPoint;
    }

    private void checkParents(List<DiscretePoint> parentPoints, DiscretePoint currentPoint) {
        int activeParentsCount = 0;
        for (DiscretePoint point : parentPoints) {
            if (activePoints.containsKey(point)) {
                activeParentsCount++;
            }
        }
        if (activeParentsCount < 2) {
            throw new ConstructionException("Fail to activate point " + currentPoint + ". Refinement should be produced level by level.");
        }
    }

    private List<DiscretePoint> getParents1(int x, int y, int k) {
        List<Integer> xList = getParentCoordinateList(x, k, ActivationType.FIRST);
        List<Integer> yList = getParentCoordinateList(y, k, ActivationType.FIRST);
        List<DiscretePoint> parents = new ArrayList<DiscretePoint>();
        for (int px : xList) {
            for (int py : yList) {
                parents.add(new DiscretePoint(px, py, k - 1));
            }
        }
        return parents;
    }

    private List<DiscretePoint> getParents2(int x, int y, int k) {
        List<Integer> xList = getParentCoordinateList(x, k, ActivationType.SECOND);
        List<Integer> yList = getParentCoordinateList(y, k, ActivationType.SECOND);
        List<DiscretePoint> parents = new ArrayList<DiscretePoint>();
        for (int px : xList) {
            parents.add(new DiscretePoint(px, y, k));
        }
        for (int py : yList) {
            parents.add(new DiscretePoint(x, py, k));
        }

        return parents;
    }

    private List<Integer> getParentCoordinateList(int c, int k, ActivationType type) {
        List<Integer> cList = new ArrayList<Integer>();

        int parentC = (c - 1);
        if (type == ActivationType.FIRST) {
            parentC /= 2;
        }
        if (inBounds(parentC, k)) {
            cList.add(parentC);
        }

        parentC = (c + 1);
        if (type == ActivationType.FIRST) {
            parentC /= 2;
        }
        if (inBounds(parentC, k)) {
            cList.add(parentC);
        }
        return cList;
    }

    private boolean inBounds(int c, int k) {
        return c >= 0 && c <= 1 << k;
    }

    private void cleanupParentEdges(List<DiscretePointDetailed> parents, DiscretePoint point) {
        for (DiscretePointDetailed p1 : parents) {
            for (DiscretePointDetailed p2 : parents) {
                DiscretePoint child = getChild(p1.point, p2.point);
                child.normalize();
                if (point.equals(child)) {
                    removeEdgeBetween(p1, p2);
                }
            }
        }
    }

    private void removeEdgeBetween(DiscretePointDetailed dpd1, DiscretePointDetailed dpd2) {
        removeEdge(dpd1, dpd2);
        removeEdge(dpd2, dpd1);
    }

    private void removeEdge(DiscretePointDetailed dpd1, DiscretePointDetailed dpd2) {
        DiscretePoint dp2 = dpd2.point;
        if (dpd1.parents != null) {
            for (int i = 0; i < dpd1.parents.size(); i++) {
                DiscretePointDetailed parent = dpd1.parents.get(i);
                if (parent.point.equals(dp2)) {
                    dpd1.parents.remove(i);
                    break;
                }
            }
        }
    }

    public DiscretePoint getChild(DiscretePoint p1, DiscretePoint p2) {
        int maxK = Math.max(p1.k, p2.k);
        p1 = p1.getNotNormalForm(maxK);
        p2 = p2.getNotNormalForm(maxK);
        return new DiscretePoint(p1.x + p2.x, p1.y + p2.y, maxK + 1);
    }

    public enum ActivationType {
        //activating parents that lays diagonally (used when point is in the center of grid cell)
        FIRST,
        //activating parents that lays vertically/horizontally (used when point is on the bound of grid cell)
        SECOND
    }
}

