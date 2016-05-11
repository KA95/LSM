package old.drawing.util;

import binary_triangulations.calculation.model.Point3D;
import old.calculation.advanced.PyramidalFunction;
import old.calculation.advanced.Triangulation;
import old.calculation.basic.LinearFunction;
import binary_triangulations.calculation.model.Point2D;
import old.calculation.basic.Triangle;
import old.calculation.basic.Triangle2D;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

import java.util.*;


public class DrawingUtil {

    public static List<Polygon> createTriangulation(Triangulation tr) {
        ArrayList<Polygon> result = new ArrayList<>();
        for (Triangle2D t : tr.getTriangles()) {
            result.add(createTriangle(new Triangle(t)));
        }
        return result;
    }

    public static List<Polygon> createApproximation(List<Triangle> triangles) {
        ArrayList<Polygon> result = new ArrayList<>();
        for (Triangle t : triangles) {
            result.add(createTriangle(t));
        }
        return result;
    }

    public static List<Polygon> createPyramidalFunctions(Collection<PyramidalFunction> functionSet, List<Point3D> point3Ds) {
        ArrayList<Polygon> result = new ArrayList<>();
        for (PyramidalFunction function : functionSet) {
            for (Triangle t : function.getParts()) {
                Point2D p = new Point2D((t.getP1().x + t.getP2().x + t.getP3().x) / 3,
                        (t.getP1().y + t.getP2().y + t.getP3().y) / 3);
                LinearFunction lf = new LinearFunction(t);
                point3Ds.add(new Point3D(p.x, p.y, lf.calculateZ(p.x, p.y)));
                result.add(createTriangle(t));
            }
        }
        return result;
    }

    public static List<Point3D> createInputDataPoints(Map<Point2D, Double> points) {
        ArrayList<Point3D> result = new ArrayList<>();
        for (Map.Entry<Point2D, Double> entry : points.entrySet()) {
            result.add(new Point3D(entry.getKey().x, entry.getKey().y, entry.getValue()));
        }
        return result;
    }

    public static Polygon createTriangle(Triangle tr) {
        return createTriangle(coordFromPoint(tr.getP1()), coordFromPoint(tr.getP2()), coordFromPoint(tr.getP3()));
    }

    public static Polygon createLine(Coord3d p1, Coord3d p2) {
        System.out.println("DrawingUtil.createLine");
        System.out.println("p1 = {" + p1.x + ", "+ p1.y + ", "+ p1.z + "}");
        System.out.println("p2 = {" + p2.x + ", "+ p2.y + ", "+ p2.z + "}");
        return createTriangle(p1, p2, p2);
    }

    public static Polygon createTriangle(Coord3d p1, Coord3d p2, Coord3d p3) {
        Polygon polygon = new Polygon();
        polygon.add(new Point(p1));
        polygon.add(new Point(p2));
        polygon.add(new Point(p3));
        polygon.setColor(new Color(0f, 1f, 1f, 0.2f));
        polygon.setWireframeColor(Color.BLACK);
        return polygon;
    }

    public static Coord3d coordFromPoint(Point3D p) {
        return new Coord3d(p.x, p.y, p.z);
    }
}