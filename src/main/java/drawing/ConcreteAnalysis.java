package drawing;

import calculation.advanced.Triangulation;
import calculation.basic.Point;
import calculation.basic.Point2D;
import calculation.basic.Triangle;
import drawing.util.DrawingUtil;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.List;
import java.util.Map;

public class ConcreteAnalysis extends AbstractAnalysis {

    Triangulation triangulation;
    Map<Point2D,Double> dataPoints;
    List<Triangle> approximation;

    public void setApproximation(List<Triangle> approximation) {
        this.approximation = approximation;
    }

    public Map<Point2D,Double> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Map<Point2D,Double> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public Triangulation getTriangulation() {
        return triangulation;
    }

    public void setTriangulation(Triangulation triangulation) {
        this.triangulation = triangulation;
    }

    public void init() {

        Shape triangulationShape = new Shape(DrawingUtil.createTriangulation(triangulation));
        Shape approximationShape = new Shape(DrawingUtil.createApproximation(approximation));
        List<Point> points = DrawingUtil.createInputDataPoints(dataPoints);

        Coord3d[] newPoints = new Coord3d[points.size()];
        for (int i = 0; i < points.size(); i++) {
            newPoints[i] = DrawingUtil.coordFromPoint(points.get(i));
        }
        Scatter scatter = new Scatter(newPoints, Color.BLACK, 1f);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());

//        chart.getScene().getGraph().add(triangulationShape);
        chart.getScene().getGraph().add(approximationShape);
//        chart.getScene().getGraph().add(scatter);
    }
}