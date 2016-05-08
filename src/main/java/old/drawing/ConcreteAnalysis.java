package old.drawing;

import old.calculation.advanced.Triangulation;
import binary_triangulations.calculation.model.Point3D;
import binary_triangulations.calculation.model.Point2D;
import old.calculation.basic.Triangle;
import old.drawing.util.DrawingUtil;
import lombok.Setter;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.List;
import java.util.Map;

@Setter
public class ConcreteAnalysis extends AbstractAnalysis {

    Triangulation triangulation;
    Map<Point2D,Double> dataPoints;
    List<Triangle> approximation;

    public void init() {
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
//        drawDomain();
        drawApproximation();
        drawPointSet();
    }

    private void drawPointSet() {
        List<Point3D> point3Ds = DrawingUtil.createInputDataPoints(dataPoints);
        Coord3d[] newPoints = new Coord3d[point3Ds.size()];
        for (int i = 0; i < point3Ds.size(); i++) {
            newPoints[i] = DrawingUtil.coordFromPoint(point3Ds.get(i));
        }
        Scatter scatter = new Scatter(newPoints, Color.BLACK, 1f);
        draw(scatter);
    }

    private void drawApproximation() {
        Shape approximationShape = new Shape(DrawingUtil.createApproximation(approximation));
        draw(approximationShape);
    }

    private void drawDomain() {
        Shape triangulationShape = new Shape(DrawingUtil.createTriangulation(triangulation));
        draw(triangulationShape);
    }

    private void draw(AbstractDrawable drawable) {
        chart.getScene().getGraph().add(drawable);
    }
}