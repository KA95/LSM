package binary_triangulations.calculation;

import binary_triangulations.calculation.model.*;
import binary_triangulations.calculation.model.basic.DiscretePoint;
import binary_triangulations.calculation.model.basic.Point2D;
import binary_triangulations.calculation.model.basic.Point3D;
import binary_triangulations.calculation.model.basic.Triangle;
import binary_triangulations.drawing.DrawingUtil;
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

    private Map<DiscretePoint, List<DiscretePoint>> neighboursMap;

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
        net.getActivePointsMap().values().stream().filter(point -> point.getParents() != null).forEach(
                point -> lines.addAll(
                        point.getParents().stream()
                                .map(parent -> DrawingUtil.createLine(getCoordinates(point.getPoint()), getCoordinates(parent.getPoint())))
                                .collect(Collectors.toList())
                )
        );
        return new Shape(lines);
    }

    public Shape buildApproximationShape(Map<DiscretePoint, Double> coefficients) {
        List<Polygon> lines = new ArrayList<>();
        net.getActivePointsMap().values().stream().filter(point -> point.getParents() != null).forEach(
                point -> lines.addAll(point.getParents().stream().map(
                        parent -> DrawingUtil.createLine(
                                getCoordinates(point.getPoint(), coefficients.get(point.getPoint())),
                                getCoordinates(parent.getPoint(), coefficients.get(parent.getPoint()))
                        )).collect(Collectors.toList())));
        return new Shape(lines);
    }

    public Shape buildPyramidalFunctionsShape() {

        List<Polygon> polygons = new ArrayList<>();

        updatePyramidalFunctionsAndNeighbours();
        for (PyramidalFunction pf : pyramidalFunctionMap.values()) {
            polygons.addAll(pf.getParts().stream().map(DrawingUtil::createTriangle).collect(Collectors.toList()));
        }
        return new Shape(polygons);
    }

    public void updatePyramidalFunctionsAndNeighbours() {
        pyramidalFunctionMap = new HashMap<>();
        neighboursMap = net.buildNeighboursSortedCounterClockwise();
        for (DiscretePoint point : net.getActivePointsMap().keySet()) {
            List<Triangle> triangles = new ArrayList<>();
            List<DiscretePoint> neighbourhood = neighboursMap.get(point);
            DiscretePoint p1 = neighbourhood.get(neighbourhood.size() - 1);
            DiscretePoint p2;
            for (DiscretePoint neighbour : neighbourhood) {
                p2 = neighbour;
                if (!DiscretePoint.angleGEthan180(p1, point, p2)) {
                    Triangle triangle = new Triangle(
                            getPoint3D(p1, 0),
                            getPoint3D(p2, 0),
                            getPoint3D(point, 1)
                    );
                    triangles.add(triangle);
                }
                p1 = p2;
            }
            pyramidalFunctionMap.put(point,
                    new PyramidalFunction(getPoint2D(point), triangles, point));
        }
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
                net.activate(new DiscretePoint(x, y, net.getGridDegree() + 1));
            }
        }
    }

    private List<Integer> getDiscreteCoordinates(double realC, double min, double max) {

        List<Integer> cList = new ArrayList<>();
        double factor = 1 << net.getGridDegree();

        double cellSize = (max - min) / factor;
        double dCoordinate = (realC - min) / cellSize;

        int c1 = (int) Math.floor(dCoordinate + EPS);
        int c2 = (int) Math.floor(dCoordinate - EPS);

        cList.add(c1 * 2 + 1);
        if (c1 != c2) {
            cList.add(c2 * 2 + 1);
        }

        return cList;
    }

    private Coord3d getCoordinates(DiscretePoint p) {
        Point3D point = getPoint3D(p, 0);
        return new Coord3d(point.x, point.y, point.z);
    }

    private Coord3d getCoordinates(DiscretePoint p, double z) {
        Point3D point = getPoint3D(p, z);
        return new Coord3d(point.x, point.y, point.z);
    }

    private Point2D getPoint2D(DiscretePoint p) {
        double factor = 1 << p.k; //2^k
        return new Point2D(p.x * (right - left) / factor + left, p.y * (top - bottom) / factor + bottom);
    }

    private Point3D getPoint3D(DiscretePoint p, double z) {
        double factor = 1 << p.k; //2^k
        return new Point3D(p.x * (right - left) / factor + left, p.y * (top - bottom) / factor + bottom, z);
    }
}

