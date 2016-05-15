package binary_triangulations;

import binary_triangulations.calculation.BinaryTriangulation;
import binary_triangulations.calculation.MainSolver;
import binary_triangulations.calculation.model.PyramidalFunction;
import binary_triangulations.calculation.model.basic.Point3D;
import binary_triangulations.calculation.model.basic.Triangle;
import binary_triangulations.drawing.BTAnalysis;
import binary_triangulations.calculation.model.basic.Point2D;
import org.jzy3d.analysis.AnalysisLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestBinary {

    private static final double TOP = 3;
    private static final double BOTTOM = -3;
    private static final double RIGHT = 3;
    private static final double LEFT = -3;

    private static final long POINTS_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        BTAnalysis btAnalysis = new BTAnalysis();

        List<Point3D> points = generatePoints();
        btAnalysis.setPoints(points);

        BinaryTriangulation triangulation = new BinaryTriangulation(LEFT, BOTTOM, RIGHT, TOP);
        btAnalysis.setTriangulation(triangulation);
        AnalysisLauncher.open(btAnalysis);

        List<Point2D> pointsToRefine;
        do {
            triangulation.updatePyramidalFunctionsAndNeighbours();

            MainSolver solver = new MainSolver(points,
                    new ArrayList<>(triangulation.getPyramidalFunctionMap().keySet()),
                    triangulation.getPyramidalFunctionMap(),
                    triangulation.getNeighboursMap());
            solver.solve();
            System.out.println("Sleeping");
            Thread.sleep(2000);
            System.out.println("Drawing...");
            btAnalysis.drawApproximation(solver.getCoefficients());
            System.out.println("Done.");
            pointsToRefine = solver.getPointsToRefine();

            for (Point2D p : pointsToRefine) {
                triangulation.refine(p.x, p.y);
            }

            triangulation.degreeUp();

        } while (pointsToRefine.size() > 0);

        System.out.println("THE END");
    }

    private static List<Point3D> generatePoints() {
        List<Point3D> points = new ArrayList<>();
        for (int i = 0; i < POINTS_COUNT; i++) {
            points.add(getRandomPointOfFunction());
        }
        return points;
    }

    private static void testTriangulation(BTAnalysis btAnalysis, BinaryTriangulation triangulation) throws InterruptedException {

        triangulation.degreeUp();
        triangulation.degreeUp();
        triangulation.degreeUp();

        Thread.sleep(4000);
        btAnalysis.updateTriangulation(triangulation);

        triangulation.refine(0.2, 0.2);
        triangulation.refine(-0.9, 0.9);
        triangulation.degreeUp();

        Thread.sleep(4000);
        btAnalysis.updateTriangulation(triangulation);

        Thread.sleep(4000);
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
