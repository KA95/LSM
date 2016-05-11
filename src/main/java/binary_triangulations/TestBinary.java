package binary_triangulations;

import binary_triangulations.calculation.BinaryTriangulation;
import binary_triangulations.calculation.model.Point3D;
import binary_triangulations.drawing.BTAnalysis;
import binary_triangulations.calculation.model.Point2D;
import org.jzy3d.analysis.AnalysisLauncher;

import java.util.ArrayList;
import java.util.List;

public class TestBinary {

    private static final double TOP = 1;
    private static final double BOTTOM = -1;
    private static final double RIGHT = 1;
    private static final double LEFT = -1;

    private static final long POINTS_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        BTAnalysis btAnalysis = new BTAnalysis();

        List<Point3D> points = generatePoints();
        btAnalysis.setPoints(points);

        BinaryTriangulation triangulation = new BinaryTriangulation(LEFT, BOTTOM, RIGHT, TOP);
        btAnalysis.setTriangulation(triangulation);
        AnalysisLauncher.open(btAnalysis);


        testTriangulation(btAnalysis, triangulation);
//
//        points.clear();
//
//        btAnalysis.updatePoints(points);

    }

    private static List<Point3D> generatePoints() {
        List<Point3D> points = new ArrayList<>();
        for (int i = 0; i < POINTS_COUNT; i++) {
            points.add(getRandomPointOfFunction());
        }
        return points;
    }

    private static void testTriangulation(BTAnalysis btAnalysis, BinaryTriangulation triangulation) throws InterruptedException {

        triangulation.refine(0.7, 0.7);
        triangulation.refine(0.2, 0.2);
        triangulation.refine(0.2, 0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7, 0.7);
        triangulation.refine(0.2, 0.2);
        triangulation.refine(0.2, 0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7, 0.7);
        triangulation.refine(0.2, 0.2);
        triangulation.refine(0.2, 0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7, 0.7);
        triangulation.refine(0.2, 0.2);
        triangulation.refine(0.2, 0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7, 0.7);
        triangulation.refine(0.2, 0.2);
        triangulation.refine(0.2, 0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7, 0.7);
        triangulation.refine(0.2, 0.2);
        triangulation.refine(0.2, 0.7);
        triangulation.degreeUp();

        btAnalysis.updateTriangulation(triangulation);

        Thread.sleep(2000);
        btAnalysis.drawPyramidalFunctions();

    }

    private static Point2D getRandomPoint() {
        double x = Math.random() * (RIGHT - LEFT) + LEFT;
        double y = Math.random() * (TOP - BOTTOM) + BOTTOM;
        return new Point2D(x, y);
    }

    private static Point3D getRandomPointOfFunction() {
        Point2D p = getRandomPoint();
        return new Point3D(p.x, p.y, f(p.x, p.y));
    }

    private static double f(double x, double y) {
        return x * Math.sin(x * y);
//        return Math.random() > 0.5 ? 1 : 0;
    }
}
