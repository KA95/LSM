package binary_triangulations.calculation;

import binary_triangulations.calculation.model.PyramidalFunction;
import binary_triangulations.calculation.model.basic.DiscretePoint;
import binary_triangulations.calculation.model.basic.Point2D;
import binary_triangulations.calculation.model.basic.Point3D;
import org.jblas.DoubleMatrix;
import org.jblas.Solve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates coefficients vector c for given point-set
 * and triangulation's pyramidal functions.
 */
public class MainSolver {

    private static final double EPS = 0.3;

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
        System.out.println("n = " + n);

        //matrix BtB
        double[][] BtB = getBTBMatrix(n);

        //column BtZ
        DoubleMatrix BtZ1 = new DoubleMatrix(getBTZColumn(n));

        //umbrella operator
        double[][] P = getPreprocessedP(n);

        //smoothing term
        double[][] E = getEMatrix(n, P);


        //smoothing parameter
        double lambda = getFrobeniusNorm(BtB, n) / getFrobeniusNorm(E, n);

        //BtB + lambda*E

        int zeros = 0;
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = BtB[i][j] + lambda * E[i][j];
                if (A[i][j] == 0) zeros++;
            }
        }

        System.out.println("zeros = " + zeros);

        System.out.println(String.format("Solving system %dx%d...", n, n));
        long start = System.nanoTime();

        DoubleMatrix A1 = new DoubleMatrix(A);
        DoubleMatrix solution = Solve.solveSymmetric(A1, BtZ1);
        long end = System.nanoTime();
        System.out.println("Solved. " + (end - start) / 1000000 + " ms");


        Map<DiscretePoint, Double> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            result.put(triangulationPoints.get(i), solution.get(i, 0));
        }

        this.coefficients = result;
        calculateDeviation();
    }

    private double getFrobeniusNorm(double[][] a, int n) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum = sum + a[i][j] * a[i][j];
            }
        }
        return Math.sqrt(sum);
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
        System.out.println("Calculating smoothing term");
        long start = System.nanoTime();
        double[][] E = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    E[i][j] = E[i][j] + P[i][k] * P[j][k];
                }
            }
        }
        long end = System.nanoTime();
        System.out.println("time : " + (end - start) / 1000000 + " ms");
        return E;
    }

    //for umbrella-operator
    private double[][] getPreprocessedP(int n) {
        System.out.println("Pre-processing for umbrella-operator");
        long start = System.nanoTime();
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
        long end = System.nanoTime();
        System.out.println("time : " + (end - start) / 1000000 + " ms");
        return P;
    }

    private double[][] getBTZColumn(int n) {
        System.out.println("calculating BtZ");
        long start = System.nanoTime();

        double[][] btz = new double[n][1];
        for (int i = 0; i < n; i++) {
            for (Point3D p : initialPoints) {
                PyramidalFunction pf = pyramidalFunctionsMap.get(triangulationPoints.get(i));
                btz[i][0] = btz[i][0] + pf.getValue(p.x, p.y) * p.z;
            }
        }

        long end = System.nanoTime();
        System.out.println("time : " + (end - start) / 1000000 + " ms");
        return btz;
    }

    private double[][] getBTBMatrix(int n) {
        System.out.println("calculating BtB");
        long start = System.nanoTime();

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
            for (int j = i; j < n; j++) {
                for (int k = 0; k < initialPoints.size(); k++) {
                    btb[i][j] = btb[i][j] + pfvalue[i][k] * pfvalue[j][k];
                    btb[j][i] = btb[i][j];
                }
            }
        }
        long end = System.nanoTime();
        System.out.println("time : " + (end - start) / 1000000 + " ms");

        return btb;
    }
}
