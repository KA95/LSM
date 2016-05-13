package binary_triangulations.calculation;

import binary_triangulations.calculation.model.PyramidalFunction;
import binary_triangulations.calculation.model.basic.DiscretePoint;
import binary_triangulations.calculation.model.basic.Point3D;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.*;

import java.util.List;
import java.util.Map;

/**
 * Calculates coefficients vector c for given point-set
 * and triangulation's pyramidal functions.
 */
@AllArgsConstructor
public class MainSolver {

    List<Point3D> initialPoints;
    List<PyramidalFunction> pyramidalFunctions;

    public Map<DiscretePoint, Double> getCoefficients() {
        int n = pyramidalFunctions.size();

        //matrix BtB
        double[][] btb = getBTBMatrix(n);

        //vector BtZ
        double[] btz = getBTZVector(n);


        //
        //todo: solve system
        //
        //matrix E
        //lambda

        //Gauss Solver
        RealMatrix coefficients =
                new Array2DRowRealMatrix(new double[][]{{1, 1, 1}, {0, 1, 1}, {1, 0, 1}},
                        false);


        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(new double[]{6, 5, 4}, false);
        RealVector solution = solver.solve(constants);

        for (int i = 0; i < solution.getDimension(); i++) {
            System.out.println(String.format("c%d: %f", i, solution.getEntry(i)));
        }

        return null;
    }

    private double[] getBTZVector(int n) {
        double[] btz = new double[n];
        for (int i = 0; i > n; i++) {
            for (Point3D p : initialPoints) {
                PyramidalFunction pf = pyramidalFunctions.get(i);
                btz[i] = btz[i] + pf.getValue(p.x, p.y) * p.z;
            }
        }
        return btz;
    }

    private double[][] getBTBMatrix(int n) {
        double[][] btb = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (Point3D p : initialPoints) {
                    PyramidalFunction pf1 = pyramidalFunctions.get(i);
                    PyramidalFunction pf2 = pyramidalFunctions.get(j);
                    btb[i][j] = btb[i][j] + pf1.getValue(p.x, p.y) * pf2.getValue(p.x, p.y);
                }
            }
        }
        return btb;
    }
}
