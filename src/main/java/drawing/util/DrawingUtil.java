package drawing.util;

import calculation.Triangulation;
import calculation.basic.Triangle;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DrawingUtil {

    public static List<Polygon> createTriangulation(Triangulation tr) {
        ArrayList<Polygon> result = new ArrayList<Polygon>();
        for(Triangle t : tr.getTriangles()) {
            result.add(createTriangle(t));
        }
        return result;
    }

    public static Polygon createTriangle(Triangle tr) {
        return createTriangle(coordFromPoint(tr.getP1()), coordFromPoint(tr.getP2()), coordFromPoint(tr.getP3()));
    }

    public static Polygon createTriangle(Coord3d p1, Coord3d p2, Coord3d p3) {
        Polygon polygon = new Polygon();
        polygon.add(new Point(p1));
        polygon.add(new Point(p2));
        polygon.add(new Point(p3));
        Random random= new Random();
        polygon.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 0.5f));
        polygon.setWireframeColor(Color.BLACK);
        return polygon;
    }

    private static Coord3d coordFromPoint(calculation.basic.Point p) {
        return new Coord3d(p.getX(), p.getY(),p.getZ());
    }
}