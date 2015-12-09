package calculation;

import calculation.basic.PyramidalFunction;
import calculation.basic.Triangle;

import java.util.ArrayList;

public class Triangulation {

    private ArrayList<Triangle> triangles;
    private ArrayList<PyramidalFunction> basisFunctions;

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public ArrayList<PyramidalFunction> getBasisFunctions() {
        return basisFunctions;
    }

    public Triangulation() {
        triangles = new ArrayList<Triangle>();
        basisFunctions = new ArrayList<PyramidalFunction>();
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
        //TODO calculate basis function
    }

    public void clearAll() {
        triangles.clear();
        basisFunctions.clear();
    }

}
