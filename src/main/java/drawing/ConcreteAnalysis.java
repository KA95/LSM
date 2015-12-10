package drawing;

import calculation.advanced.Triangulation;
import calculation.basic.Point;
import drawing.util.DrawingUtil;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.List;

public class ConcreteAnalysis extends AbstractAnalysis {
    Triangulation triangulation;

    public Triangulation getTriangulation() {
        return triangulation;
    }

    public void setTriangulation(Triangulation triangulation) {
        this.triangulation = triangulation;
    }

    public void init() {

        Shape triangulationShape = new Shape(DrawingUtil.createTriangulation(triangulation));
        List<Point> points = new ArrayList<Point>();
        Shape pyramidalFunctions = new Shape(DrawingUtil.createPyramidalFunctions(triangulation.getVertices().values(),points));

        Coord3d[] newPoints = new Coord3d[points.size()];
        for(int i=0;i<points.size();i++) {
            newPoints[i]=DrawingUtil.coordFromPoint(points.get(i));
        }
        Scatter scatter = new Scatter(newPoints, Color.BLACK, 0.5f);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());

        chart.getScene().getGraph().add(triangulationShape);
        chart.getScene().getGraph().add(pyramidalFunctions);
        chart.getScene().getGraph().add(scatter);
    }
}