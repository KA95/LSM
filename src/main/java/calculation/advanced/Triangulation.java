package calculation.advanced;

import calculation.basic.Point;
import calculation.basic.Triangle;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Triangulation {

    private Set<Triangle> triangles;
    private Map<Point, PyramidalFunction> vertices;

    public Set<Triangle> getTriangles() {
        return triangles;
    }

    public Triangulation() {
        triangles = new HashSet<Triangle>();
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public void rebuildVertices() {
        vertices = new TreeMap<Point, PyramidalFunction>();
        for(Triangle t : triangles) {
            for(Point p : t.getVerticesList()) {
                if (!vertices.containsKey(p)) {
                    vertices.put(p, null);
                }
            }
        }
    }

    public void clearAll() {
        triangles.clear();
    }

    public void fullSplitting() {
        Set<Triangle> oldTriangles = triangles;
        triangles = null;
        triangles = new HashSet<Triangle>();
        for(Triangle tr : oldTriangles) {
            for(Triangle t : tr.split()) {
                addTriangle(t);
            }
        }
        rebuildVertices();
    }

}
