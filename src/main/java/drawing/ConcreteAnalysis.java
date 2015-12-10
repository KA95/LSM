package drawing;

import calculation.advanced.Triangulation;
import drawing.util.DrawingUtil;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

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

        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());

        chart.getScene().getGraph().add(triangulationShape);
    }
}