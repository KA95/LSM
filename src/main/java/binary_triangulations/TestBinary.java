package binary_triangulations;

import binary_triangulations.calculation.BinaryTriangulation;
import binary_triangulations.calculation.model.DiscretePoint;
import binary_triangulations.drawing.BTAnalysis;
import binary_triangulations.calculation.model.Point2D;
import org.jzy3d.analysis.AnalysisLauncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestBinary {

    private static final double TOP = 1;
    private static final double BOTTOM = 0;
    private static final double RIGHT = 1;
    private static final double LEFT = 0;

    private static final long POINTS_COUNT = 10;


    public static void main(String[] args) throws Exception {
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0, 0));
        points.add(new Point2D(1, 1));
        BTAnalysis btAnalysis = new BTAnalysis();
        btAnalysis.setPoints(points);

        BinaryTriangulation triangulation = new BinaryTriangulation(LEFT, BOTTOM, RIGHT, TOP);
        btAnalysis.setTriangulation(triangulation);
        AnalysisLauncher.open(btAnalysis);

        testTriangulation(btAnalysis, triangulation);

    }

    private static void testTriangulation(BTAnalysis btAnalysis, BinaryTriangulation triangulation) throws InterruptedException {

        triangulation.refine(0.7,0.7);
        triangulation.refine(0.2,0.2);
        triangulation.refine(0.2,0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7,0.7);
        triangulation.refine(0.2,0.2);
        triangulation.refine(0.2,0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7,0.7);
        triangulation.refine(0.2,0.2);
        triangulation.refine(0.2,0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7,0.7);
        triangulation.refine(0.2,0.2);
        triangulation.refine(0.2,0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7,0.7);
        triangulation.refine(0.2,0.2);
        triangulation.refine(0.2,0.7);
        triangulation.degreeUp();

        triangulation.refine(0.7,0.7);
        triangulation.refine(0.2,0.2);
        triangulation.refine(0.2,0.7);
        triangulation.degreeUp();

        btAnalysis.updateTriangulation(triangulation);

        Thread.sleep(2000);
        btAnalysis.drawPyramidalFunctions();

    }

    private static void testDynamicAddingPoints(List<Point2D> points, BTAnalysis btAnalysis) throws Exception {
        for (int i = 0; i < POINTS_COUNT; i++) {
            points.add(getRandomPoint());
            System.out.println("i = " + i);
            Thread.sleep(2000);
            btAnalysis.updatePoints(points);
            //triangulation for them
        }
    }

    private static Point2D getRandomPoint() {
        double x = Math.random();
        double y = Math.random();
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        return new Point2D(x, y);
    }
}
