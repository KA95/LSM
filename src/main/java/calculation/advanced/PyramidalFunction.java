package calculation.advanced;

import calculation.basic.Point;
import calculation.basic.PyramidalFunctionPart;

import java.util.ArrayList;
import java.util.List;

public class PyramidalFunction {
    Point center;
    List<PyramidalFunctionPart> parts;

    PyramidalFunction(Point center, PyramidalFunctionPart part) {
        this.center = center;
        parts = new ArrayList<PyramidalFunctionPart>();
        parts.add(part);
    }
}
