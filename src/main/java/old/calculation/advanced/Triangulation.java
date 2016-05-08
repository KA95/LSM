package old.calculation.advanced;

import binary_triangulations.calculation.model.Point2D;
import old.calculation.basic.Triangle;
import old.calculation.basic.Triangle2D;

import java.util.*;

public class Triangulation {

    private Set<Triangle2D> triangles;
    private Map<Point2D, PyramidalFunction> vertices;

    public Map<Point2D, PyramidalFunction> getVertices() {
        return vertices;
    }

    public Set<Triangle2D> getTriangles() {
        return triangles;
    }

    public Triangulation() {
        triangles = new HashSet<Triangle2D>();
    }

    public void addTriangle(Triangle2D triangle) {
        triangles.add(triangle);
    }

    public void rebuildVertices() {
        vertices = new TreeMap<Point2D, PyramidalFunction>();
        for(Triangle2D t : triangles) {
            updatePyramidalFunctionP1(t);
            updatePyramidalFunctionP2(t);
            updatePyramidalFunctionP3(t);
        }
    }

    private void updatePyramidalFunctionP1(Triangle2D t) {
        Point2D p1 = t.getP1();
        Triangle t1 = new Triangle(t);
        t1.getP1().setZ(1);
        if (!vertices.containsKey(p1)) {
            vertices.put(p1, new PyramidalFunction(p1,t1));
        } else {
            vertices.get(p1).addPart(t1);
        }
    }

    private void updatePyramidalFunctionP2(Triangle2D t) {
        Point2D p2 = t.getP2();
        Triangle t1 = new Triangle(t);
        t1.getP2().setZ(1);
        if (!vertices.containsKey(p2)) {
            vertices.put(p2, new PyramidalFunction(p2,t1));
        } else {
            vertices.get(p2).addPart(t1);
        }
    }

    private void updatePyramidalFunctionP3(Triangle2D t) {
        Point2D p3 = t.getP3();
        Triangle t1 = new Triangle(t);
        t1.getP3().setZ(1);
        if (!vertices.containsKey(p3)) {
            vertices.put(p3, new PyramidalFunction(p3,t1));
        } else {
            vertices.get(p3).addPart(t1);
        }
    }

    public void fullSplitting() {
        Set<Triangle2D> oldTriangles = triangles;
        triangles = null;
        triangles = new HashSet<Triangle2D>();
        for(Triangle2D tr : oldTriangles) {
            for(Triangle2D t : tr.split()) {
                addTriangle(t);
            }
        }
        rebuildVertices();
    }


}
