import calculation.advanced.Triangulation;
import calculation.basic.Point;
import calculation.basic.Triangle;
import drawing.ConcreteAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;

public class Main {
    static final int TIMES = 1;
    public static void main(String[] args) throws Exception {
        Triangulation triangulation = initTriangulation();
        triangulation.rebuildVertices();
        for(int i=0;i<2*TIMES;i++) {
            triangulation.fullSplitting();
        }
        ConcreteAnalysis concreteAnalysis = new ConcreteAnalysis();
        concreteAnalysis.setTriangulation(triangulation);
        AnalysisLauncher.open(concreteAnalysis);

    }

    private static Triangulation initTriangulation () {
        Point p0 = new Point(0,0);
        Point p1 = new Point(1,1);
        Point p2 = new Point(1,-1);
        Point p3 = new Point(-1,-1);
        Point p4 = new Point(-1,1);

        Triangulation tr = new Triangulation();
        tr.addTriangle(new Triangle(p0,p1,p2));
        tr.addTriangle(new Triangle(p0,p2,p3));
        tr.addTriangle(new Triangle(p0,p3,p4));
        tr.addTriangle(new Triangle(p0,p4,p1));
        return tr;
    }
}