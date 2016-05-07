package drawing.util;

import calculation.advanced.PyramidalFunction;
import calculation.advanced.Triangulation;
import calculation.basic.LinearFunction;
import calculation.basic.Point2D;
import calculation.basic.Triangle;
import calculation.basic.Triangle2D;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

import java.util.*;


public class DrawingUtil {

    public static List<Polygon> createTriangulation(Triangulation tr) {
        ArrayList<Polygon> result = new ArrayList<Polygon>();
        for (Triangle2D t : tr.getTriangles()) {
            result.add(createTriangle(new Triangle(t)));
        }
        return result;
    }

    public static List<Polygon> createApproximation(List<Triangle> triangles) {
        ArrayList<Polygon> result = new ArrayList<Polygon>();
        for (Triangle t : triangles) {
            result.add(createTriangle(t));
        }
        return result;
    }

    public static List<Polygon> createPyramidalFunctions(Collection<PyramidalFunction> functionSet, List<calculation.basic.Point> points) {
        ArrayList<Polygon> result = new ArrayList<Polygon>();
        for (PyramidalFunction function : functionSet) {
            for (Triangle t : function.getParts()) {
                Point2D p = new Point2D((t.getP1().getX() + t.getP2().getX() + t.getP3().getX()) / 3,
                        (t.getP1().getY() + t.getP2().getY() + t.getP3().getY()) / 3);
                LinearFunction lf = new LinearFunction(t);
                points.add(new calculation.basic.Point(p.getX(), p.getY(), lf.calculateZ(p.getX(), p.getY())));
                result.add(createTriangle(t));
            }
        }
        return result;
    }

    public static List<calculation.basic.Point> createInputDataPoints(Map<Point2D, Double> points) {
        ArrayList<calculation.basic.Point> result = new ArrayList<calculation.basic.Point>();
        for (Map.Entry<Point2D, Double> entry : points.entrySet()) {
            result.add(new calculation.basic.Point(entry.getKey().getX(), entry.getKey().getY(), entry.getValue()));
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

    public static Coord3d coordFromPoint(calculation.basic.Point p) {
        return new Coord3d(p.getX(), p.getY(), p.getZ());
    }
}