package binary_triangulations.drawing;

import binary_triangulations.calculation.BinaryTriangulation;
import binary_triangulations.calculation.model.DiscretePoint;
import binary_triangulations.calculation.model.Point2D;
import lombok.Setter;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.List;

@Setter
public class BTAnalysis extends AbstractAnalysis {
    BinaryTriangulation triangulation;

    List<Point2D> points;
    Scatter pointsScatter;

    public void init() {
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        drawPointSet();
        draw(triangulation.buildTriangulationShape());
    }

    public void updateTriangulation(BinaryTriangulation triangulation) {
//        chart.removeDrawable(triangulation.buildTriangulationShape());
        this.triangulation = triangulation;
        draw(triangulation.buildTriangulationShape());
        chart.updateProjectionsAndRender();
    }

    public void drawPyramidalFunctions() {
        draw(triangulation.buildPyramidalFunctionsShape());
        chart.updateProjectionsAndRender();
    }

    public void updatePoints(List<Point2D> points) {
        chart.removeDrawable(pointsScatter);
        this.points = points;
        drawPointSet();
        chart.updateProjectionsAndRender();
    }

    private void drawPointSet() {
        Coord3d[] newPoints = new Coord3d[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point2D current = points.get(i);
            newPoints[i] = new Coord3d(current.getX(), current.getY(), 0);
        }
        pointsScatter = new Scatter(newPoints, Color.BLACK, 3f);
        draw(pointsScatter);
    }

    private void draw(AbstractDrawable drawable) {
        chart.getScene().getGraph().add(drawable);
    }
}
