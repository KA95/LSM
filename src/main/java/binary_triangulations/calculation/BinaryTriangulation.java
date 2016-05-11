package binary_triangulations.calculation;

import binary_triangulations.calculation.model.ActivationType;
import binary_triangulations.calculation.model.DiscreteNet;
import binary_triangulations.calculation.model.DiscretePoint;
import binary_triangulations.calculation.model.DiscretePointDetailed;
import old.calculation.advanced.PyramidalFunction;
import old.drawing.util.DrawingUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class BinaryTriangulation {

    private static final double EPS = 1.0e-9;

    private double left;
    private double right;
    private double top;
    private double bottom;

    private DiscreteNet net;

    private Map<DiscretePoint, PyramidalFunction> pyramidalFunctionMap;

    public BinaryTriangulation(double left, double bottom, double right, double top) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

        net = new DiscreteNet();
    }

    /**
     * Rebuild drawable shape.
     */
    public Shape buildTriangulationShape() {
        List<Polygon> lines = new ArrayList<>();
        int v = 0;
        int e = 0;
        for (DiscretePointDetailed point : net.getActivePointsMap().values()) {
            v++;
            if (point.getParents() != null) {
                for (DiscretePointDetailed parent : point.getParents()) {
                    e++;
                    lines.add(DrawingUtil.createLine(getCoordinates(point.getPoint()), getCoordinates(parent.getPoint())));
                }
            }
        }
        System.out.println("e = " + e);
        System.out.println("v = " + v);
        return new Shape(lines);
    }

    public Shape buildPyramidalFunctionsShape() {
        Map<DiscretePoint, List<DiscretePoint>> neighboursMap = net.buildNeighboursSortedCounterClockwise();
        List<Polygon> triangles = new ArrayList<>();
        for (DiscretePoint point : net.getActivePointsMap().keySet()) {
            List<DiscretePoint> neighbourhood = neighboursMap.get(point);
            DiscretePoint p1 = neighbourhood.get(neighbourhood.size() - 1);
            DiscretePoint p2;
            for (DiscretePoint neighbour : neighbourhood) {
                p2 = neighbour;
                if (!DiscretePoint.angleGEthan180(p1, point, p2)) {
                    triangles.add(DrawingUtil.createTriangle(
                            getCoordinates(p1, 1),
                            getCoordinates(p2, 1),
                            getCoordinates(point, 3)
                    ));
                }
                p1 = p2;
            }
        }
        return new Shape(triangles);
    }

    public void degreeUp() {
        net.degreeUp();
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
                net.activate(new DiscretePoint(x, y, net.getGridDegree() + 1), ActivationType.FIRST);
            }
        }
    }

    private List<Integer> getDiscreteCoordinates(double realC, double min, double max) {

        List<Integer> cList = new ArrayList<>();
        double factor = 1 << net.getGridDegree();

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

    private Coord3d getCoordinates(DiscretePoint p) {
        double factor = 1 << p.k; //2^k
        return new Coord3d(p.x * (right - left) / factor + left, p.y * (top - bottom) / factor + bottom, 0);
    }

    private Coord3d getCoordinates(DiscretePoint p, double z) {
        double factor = 1 << p.k; //2^k
        return new Coord3d(p.x * (right - left) / factor + left, p.y * (top - bottom) / factor + bottom, z);
    }
}

