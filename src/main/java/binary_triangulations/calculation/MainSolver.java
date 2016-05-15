package binary_triangulations.calculation;

import binary_triangulations.calculation.model.PyramidalFunction;
import binary_triangulations.calculation.model.basic.DiscretePoint;
import binary_triangulations.calculation.model.basic.Point2D;
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
public class MainSolver {

    private static final double EPS = 0.1;

    private List<Point3D> initialPoints;
    private List<DiscretePoint> triangulationPoints;
    private Map<DiscretePoint, PyramidalFunction> pyramidalFunctionsMap;
    private Map<DiscretePoint, List<DiscretePoint>> neighboursMap;

    private Map<DiscretePoint, Double> coefficients = null;

    private List<Double> deviation = null;

    private List<Point2D> pointsToRefine = null;

    public Map<DiscretePoint, Double> getCoefficients() {
        return coefficients;
    }

    public List<Double> getDeviation() {
        return deviation;
    }

    public List<Point2D> getPointsToRefine() {
        return pointsToRefine;
    }

    public MainSolver(List<Point3D> initialPoints, List<DiscretePoint> triangulationPoints, Map<DiscretePoint, PyramidalFunction> pyramidalFunctionsMap, Map<DiscretePoint, List<DiscretePoint>> neighboursMap) {
        this.initialPoints = initialPoints;
        this.triangulationPoints = triangulationPoints;
        this.pyramidalFunctionsMap = pyramidalFunctionsMap;
        this.neighboursMap = neighboursMap;
    }

    public void solve() {
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
        double lambda = BtB.getFrobeniusNorm() / E.getFrobeniusNorm() * 10;

        System.out.println(String.format("Solving system %dx%d...", n, n));
        long start = System.nanoTime();
        RealMatrix coefficients = BtB.add(E.scalarMultiply(lambda));
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector solution = solver.solve(BtZ);
        long end = System.nanoTime();
        System.out.println("Solved. " + (end - start)/1000000 + " ms");

        Map<DiscretePoint, Double> result = new HashMap<>();
        for (int i = 0; i < solution.getDimension(); i++) {
            System.out.println(String.format("c%d: %f", i, solution.getEntry(i)));
            result.put(triangulationPoints.get(i), solution.getEntry(i));
        }

        this.coefficients = result;
        calculateDeviation();
    }

    private void calculateDeviation() {
        System.out.println("Calculating deviation.");
        long start = System.nanoTime();
        List<Double> deviation = new ArrayList<>();
        List<Point2D> pointsToRefine = new ArrayList<>();
        for (Point3D p3d : initialPoints) {
            double f = 0;
            for (DiscretePoint p : triangulationPoints) {
                f = f + coefficients.get(p) * pyramidalFunctionsMap.get(p).getValue(p3d.x, p3d.y);
            }
            double d = Math.abs(f - p3d.z);
            deviation.add(d);
            if (d > EPS) {
                pointsToRefine.add(new Point2D(p3d));
            }
        }
        this.deviation = deviation;
        this.pointsToRefine = pointsToRefine;
        long end = System.nanoTime();
        System.out.println("Finished. " + (end - start) / 1000000 + " ms");
    }

    private double[][] getEMatrix(int n, double[][] P) {
        double[][] E = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    E[i][j] = E[i][j] + P[i][k] * P[j][k];
                }
            }
        }
        return E;
    }

    //for umbrella-operator
    private double[][] getPreprocessedP(int n) {
        double[][] P = new double[n][n];

        for (int l = 0; l < n; l++) {
            for (int k = 0; k < n; k++) {
                if (l == k) {
                    P[l][k] = -1;
                } else if (neighboursMap.get(triangulationPoints.get(l)).contains(triangulationPoints.get(k))) {
                    P[l][k] = 1.0 / neighboursMap.get(triangulationPoints.get(k)).size();
                } else {
                    P[l][k] = 0;
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
