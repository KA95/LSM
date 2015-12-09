package drawing;

import calculation.Triangulation;
import calculation.basic.Point;
import calculation.basic.Triangle;
import drawing.util.DrawingUtil;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;

public class ConcreteAnalysis extends AbstractAnalysis {

    double step = 0.1;
    ArrayList<Coord3d> pointsList = new ArrayList<Coord3d>();

    public void init() {
        // Define a function to plot

        for (int i=-10;i<=10; i++) {
            for (int j = -10; j <= 10; j++) {
                pointsList.add(new Coord3d(step * i, step * j, 0));
            }
        }

        Coord3d[] newPoints = new Coord3d[pointsList.size()];
        for(int i=0;i<pointsList.size();i++) {
            newPoints[i]=pointsList.get(i);
        }
        Scatter scatter = new Scatter(newPoints, Color.BLACK, 0.1f);

        Point p0,p1,p2,p3,p4;
        p0 = new Point(0,0);
        p1 = new Point(1,1);
        p2 = new Point(-1,1);
        p3 = new Point(-1,-1);
        p4 = new Point(1,-1);
        Triangulation triangulation = new Triangulation();
        triangulation.addTriangle(new Triangle(p0,p1,p2));
        triangulation.addTriangle(new Triangle(p0,p2,p3));
        triangulation.addTriangle(new Triangle(p0,p3,p4));
        triangulation.addTriangle(new Triangle(p0,p1,p4));

        Shape triangulationShape = new Shape(DrawingUtil.createTriangulation(triangulation));

        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());

        chart.getScene().getGraph().add(triangulationShape);
        chart.getScene().getGraph().add(scatter);
    }
}