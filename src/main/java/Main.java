import calculation.advanced.Triangulation;
import calculation.basic.Point2D;
import calculation.basic.Triangle2D;
import drawing.ConcreteAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;

public class Main {
    static final int TRIANGULATION_DEGREE = 0;

    public static void main(String[] args) throws Exception {
        Triangulation triangulation = initTriangulation();
        triangulation.rebuildVertices();
        for (int i = 0; i < 2 * TRIANGULATION_DEGREE; i++) {
            triangulation.fullSplitting();
        }
        System.out.println(triangulation.getTriangles().size());
        ConcreteAnalysis concreteAnalysis = new ConcreteAnalysis();
        concreteAnalysis.setTriangulation(triangulation);
        AnalysisLauncher.open(concreteAnalysis);
    }

    private static Triangulation initTriangulation() {
        Point2D p0 = new Point2D(0, 0);
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(1, -1);
        Point2D p3 = new Point2D(-1, -1);
        Point2D p4 = new Point2D(-1, 1);

        Triangulation tr = new Triangulation();
        tr.addTriangle(new Triangle2D(p0, p1, p2));
        tr.addTriangle(new Triangle2D(p0, p2, p3));
        tr.addTriangle(new Triangle2D(p0, p3, p4));
        tr.addTriangle(new Triangle2D(p0, p4, p1));
        return tr;
    }
}