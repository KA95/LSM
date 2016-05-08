package old;

import binary_triangulations.calculation.model.Point3D;
import old.calculation.advanced.Triangulation;
import binary_triangulations.calculation.model.Point2D;
import old.calculation.basic.Triangle;
import old.calculation.basic.Triangle2D;
import old.drawing.ConcreteAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    private static final int TRIANGULATION_DEGREE = 4;
    private static final int DATA_DEGREE = 6;
    private static final double TOP = 3;
    private static final double BOTTOM = -3;
    private static final double RIGHT = 3;
    private static final double LEFT = -3;

    public static void main(String[] args) throws Exception {
        Map<Point2D, Double> inputData = initDataPoints();
        Triangulation triangulation = initTriangulation();
        triangulation.rebuildVertices();
        for (int i = 0; i < 2 * TRIANGULATION_DEGREE; i++) {
            triangulation.fullSplitting();
        }
        ConcreteAnalysis concreteAnalysis = new ConcreteAnalysis();
        concreteAnalysis.setTriangulation(triangulation);
        concreteAnalysis.setDataPoints(inputData);
        concreteAnalysis.setApproximation(buildApproximation(inputData, triangulation));
        AnalysisLauncher.open(concreteAnalysis);
    }

    /**
     * Initialize triangulation of degree 1 with preset boundaries on x-axis and y-axis.
     *
     * @return built triangulation
     */
    private static Triangulation initTriangulation() {
        Point2D p0 = new Point2D((LEFT + RIGHT) / 2, (TOP + BOTTOM) / 2);
        Point2D p1 = new Point2D(RIGHT, TOP);
        Point2D p2 = new Point2D(RIGHT, BOTTOM);
        Point2D p3 = new Point2D(LEFT, BOTTOM);
        Point2D p4 = new Point2D(LEFT, TOP);

        Triangulation tr = new Triangulation();
        tr.addTriangle(new Triangle2D(p0, p1, p2));
        tr.addTriangle(new Triangle2D(p0, p2, p3));
        tr.addTriangle(new Triangle2D(p0, p3, p4));
        tr.addTriangle(new Triangle2D(p0, p4, p1));
        return tr;
    }

    /**
     * Initialize data in points laying exactly for triangulation by function.
     *
     * @return map (point in domain -> value)
     */
    private static Map<Point2D, Double> initDataPoints() {
        int count = (int) Math.pow(2, DATA_DEGREE);
        double step = (TOP - BOTTOM) / Math.pow(2, DATA_DEGREE);
        Map<Point2D, Double> result = new TreeMap<Point2D, Double>();
        for (int i = 0; i <= count; i++) {
            for (int j = 0; j <= count; j++) {
                double x, y;
                x = LEFT + step * i;
                y = BOTTOM + step * j;
                result.put(new Point2D(x, y), f(x, y));
            }
        }
        return result;
    }

    private static List<Triangle> buildApproximation(Map<Point2D, Double> inputData, Triangulation triangulation) {
        List<Triangle> result = new ArrayList<Triangle>();
        for (Triangle2D t : triangulation.getTriangles()) {
            Point3D p1, p2, p3;
            p1 = new Point3D(t.getP1());
            p2 = new Point3D(t.getP2());
            p3 = new Point3D(t.getP3());
            p1.setZ(inputData.get(t.getP1()));
            p2.setZ(inputData.get(t.getP2()));
            p3.setZ(inputData.get(t.getP2()));
            Triangle newT = new Triangle(p1, p2, p3);
            result.add(newT);
        }
        return result;

    }

    /**
     * Function to get z-coordinate of input point set by coordinates of point from domain.
     *
     * @param x coordinate
     * @param y coordinate
     * @return z-coordinate
     */
    private static double f(double x, double y) {
        return x * Math.sin(x * y);
//        return Math.random() > 0.5 ? 1 : 0;
    }

}