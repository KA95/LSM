package binary_triangulations.calculation;

import binary_triangulations.calculation.model.PyramidalFunction;
import binary_triangulations.calculation.model.basic.DiscretePoint;
import binary_triangulations.calculation.model.basic.Point3D;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.*;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates coefficients vector c for given point-set
 * and triangulation's pyramidal functions.
 */
@AllArgsConstructor
public class MainSolver {

    private List<Point3D> initialPoints;
    private List<DiscretePoint> triangulationPoints;
    private Map<DiscretePoint, PyramidalFunction> pyramidalFunctionsMap;
    private Map<DiscretePoint, List<DiscretePoint>> neighboursMap;

    public Map<DiscretePoint, Double> getCoefficients() {
        int n = triangulationPoints.size();

        //matrix BtB
        RealMatrix BtB = new Array2DRowRealMatrix(getBTBMatrix(n));

        //vector BtZ
        RealVector BtZ = new ArrayRealVector(getBTZVector(n));

        //umbrella operator
        double[][] P = getPreprocessedP(n);

        //smoothing term
        RealMatrix E = new Array2DRowRealMatrix(getEMatrix(n, P));

        //smoothing parameter
        double lambda = BtB.getFrobeniusNorm() / E.getFrobeniusNorm();

        RealMatrix coefficients = BtB.add(E.scalarMultiply(lambda));
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

        RealVector solution = solver.solve(BtZ);

        Map<DiscretePoint, Double> result = new HashMap<>();
        for (int i = 0; i < solution.getDimension(); i++) {
            System.out.println(String.format("c%d: %f", i, solution.getEntry(i)));
            result.put(triangulationPoints.get(i), solution.getEntry(i));
        }
        return result;
    }

    private double[][] getEMatrix(int n, double[][] P) {
        double[][] E = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    E[i][j] = P[i][k] * P[j][k];
                }
            }
        }
        return E;
    }

    //for umbrella-operator
    private double[][] getPreprocessedP(int n) {
        double[][] P = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    P[i][j] = -1;
                } else if (neighboursMap.get(triangulationPoints.get(i)).contains(triangulationPoints.get(j))) {
                    P[i][j] = 1.0 / neighboursMap.get(triangulationPoints.get(j)).size();
                } else {
                    P[i][j] = 0;
                }
            }
        }

        return P;
    }

    private double[] getBTZVector(int n) {
        double[] btz = new double[n];
        for (int i = 0; i < n; i++) {
            for (Point3D p : initialPoints) {
                PyramidalFunction pf = pyramidalFunctionsMap.get(triangulationPoints.get(i));
                btz[i] = btz[i] + pf.getValue(p.x, p.y) * p.z;
            }
        }
        return btz;
    }

    private double[][] getBTBMatrix(int n) {
        double[][] btb = new double[n][n];


        double[][] pfvalue = new double[n][initialPoints.size()];

        List<PyramidalFunction> pf = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            pf.add(pyramidalFunctionsMap.get(triangulationPoints.get(i)));
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < initialPoints.size(); j++) {
                Point3D p = initialPoints.get(j);
                pfvalue[i][j] = pf.get(i).getValue(p.x, p.y);
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println("i = " + i);
            long start = System.nanoTime();
            for (int j = i; j < n; j++) {
                for (int k = 0; k < initialPoints.size(); k++) {
                    btb[i][j] = btb[i][j] + pfvalue[i][k] * pfvalue[j][k];
                    btb[j][i] = btb[i][j];
                }
            }
            long end = System.nanoTime();
            System.out.println("time : " + (end - start) / 1000000 + " ms");
        }

        return btb;
    }
}
