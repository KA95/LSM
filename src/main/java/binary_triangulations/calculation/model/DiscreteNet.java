package binary_triangulations.calculation.model;


import binary_triangulations.exception.ConstructionException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class DiscreteNet {

    private int gridDegree = 0; //(2^gridDegree x 2^gridDegree) regular grid

    private Map<DiscretePoint, DiscretePointDetailed> activePointsMap = new HashMap<>();

    public DiscreteNet() {
        DiscretePointDetailed p1 = new DiscretePointDetailed(1, 0, 0, null);
        DiscretePointDetailed p2 = new DiscretePointDetailed(0, 1, 0, null);
        ArrayList<DiscretePointDetailed> list1 = new ArrayList<>(Arrays.asList(p1, p2));
        DiscretePointDetailed p3 = new DiscretePointDetailed(1, 1, 0, list1);
        ArrayList<DiscretePointDetailed> list2 = new ArrayList<>(Arrays.asList(p1, p2, p3));
        DiscretePointDetailed p4 = new DiscretePointDetailed(0, 0, 0, list2);
        activePointsMap.put(p1.getPoint(), p1);
        activePointsMap.put(p2.getPoint(), p2);
        activePointsMap.put(p3.getPoint(), p3);
        activePointsMap.put(p4.getPoint(), p4);
        activate(new DiscretePoint(1, 1, 1), ActivationType.FIRST);
        gridDegree = 1;
    }

    public void degreeUp() {
        gridDegree++;
    }

    /**
     * Activate points recursively with saving triangulation correctness.
     *
     * @param point point to activate
     * @param type  activation type
     * @return new point of triangulation
     */
    public DiscretePointDetailed activate(DiscretePoint point, ActivationType type) {
        List<DiscretePoint> parentPoints;
        if (type == ActivationType.FIRST) {
            parentPoints = getParents1(point.x, point.y, point.k);
        } else {
            parentPoints = getParents2(point.x, point.y, point.k);
        }
        checkParents(parentPoints, point);
        ArrayList<DiscretePointDetailed> parents = new ArrayList<>();
        for (DiscretePoint parentPoint : parentPoints) {
            DiscretePointDetailed parentDetailed =
                    activePointsMap.containsKey(parentPoint)
                            ? activePointsMap.get(parentPoint)
                            : (type == ActivationType.FIRST
                            ? activate(parentPoint, ActivationType.SECOND)
                            : activate(parentPoint, ActivationType.FIRST)
                    );
            parents.add(parentDetailed);
        }
        cleanupParentEdges(parents, point);
        DiscretePointDetailed newPoint = new DiscretePointDetailed(point, parents);
        activePointsMap.put(newPoint.getPoint(), newPoint);
        return newPoint;
    }

    public Map<DiscretePoint, List<DiscretePoint>> buildNeighboursSortedCounterClockwise() {
        Map<DiscretePoint, List<DiscretePoint>> result = buildNeighbours();
        for (Map.Entry<DiscretePoint, List<DiscretePoint>> entry : result.entrySet()) {
            sortPointsCounterClockwise(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private void sortPointsCounterClockwise(DiscretePoint center, List<DiscretePoint> points) {
        Collections.sort(points, (p1, p2) -> {
            int maxK = Math.max(center.k, Math.max(p1.k, p2.k));

            DiscretePoint cnn = center.getNotNormalForm(maxK);
            DiscretePoint pnn1 = p1.getNotNormalForm(maxK);
            DiscretePoint pnn2 = p2.getNotNormalForm(maxK);

            pnn1.x -= cnn.x;
            pnn1.y -= cnn.y;
            pnn2.x -= cnn.x;
            pnn2.y -= cnn.y;

            int prod = pnn1.x * pnn2.x;

            if (prod > 0 || (pnn1.x == 0 && pnn2.x == 0) || (pnn1.x < 0 && pnn2.x == 0) || (pnn1.x == 0 && pnn2.x < 0)) {
                // same half
                // then sort by tangent (y1/x1 - y2/x2)
                // transform to (y1x2 - y2*x1)
                // x = 0 will correspond to left half
                // to avoid error move left half from x to (x-1)
                if (pnn1.x <= 0) {
                    return pnn1.y * (pnn2.x - 1) - pnn2.y * (pnn1.x - 1);
                } else {
                    return pnn1.y * (pnn2.x) - pnn2.y * (pnn1.x);
                }
            } else {
                return pnn1.x - pnn2.x;
            }
        });
    }

    private void checkParents(List<DiscretePoint> parentPoints, DiscretePoint currentPoint) {
        int activeParentsCount = 0;
        for (DiscretePoint point : parentPoints) {
            if (activePointsMap.containsKey(point)) {
                activeParentsCount++;
            }
        }
        if (activeParentsCount < 2) {
            throw new ConstructionException("Fail to activate point " + currentPoint + ". Refinement should be produced level by level.");
        }
    }

    private List<DiscretePoint> getParents1(int x, int y, int k) {
        List<Integer> xList = getParentCoordinateList(x, k, ActivationType.FIRST);
        List<Integer> yList = getParentCoordinateList(y, k, ActivationType.FIRST);
        List<DiscretePoint> parents = new ArrayList<>();
        for (int px : xList) {
            parents.addAll(yList.stream().map(py -> new DiscretePoint(px, py, k - 1)).collect(Collectors.toList()));
        }
        return parents;
    }

    private List<DiscretePoint> getParents2(int x, int y, int k) {
        List<Integer> xList = getParentCoordinateList(x, k, ActivationType.SECOND);
        List<Integer> yList = getParentCoordinateList(y, k, ActivationType.SECOND);
        List<DiscretePoint> parents = xList.stream().map(px -> new DiscretePoint(px, y, k)).collect(Collectors.toList());
        parents.addAll(yList.stream().map(py -> new DiscretePoint(x, py, k)).collect(Collectors.toList()));

        return parents;
    }

    private List<Integer> getParentCoordinateList(int c, int k, ActivationType type) {
        List<Integer> cList = new ArrayList<>();

        int parentC = (c - 1);
        if (type == ActivationType.FIRST) {
            parentC /= 2;
        }
        if (inBounds(parentC, k)) {
            cList.add(parentC);
        }

        parentC = (c + 1);
        if (type == ActivationType.FIRST) {
            parentC /= 2;
        }
        if (inBounds(parentC, k)) {
            cList.add(parentC);
        }
        return cList;
    }

    private boolean inBounds(int c, int k) {
        return c >= 0 && c <= 1 << k;
    }

    private void cleanupParentEdges(List<DiscretePointDetailed> parents, DiscretePoint point) {
        for (DiscretePointDetailed p1 : parents) {
            for (DiscretePointDetailed p2 : parents) {
                DiscretePoint child = getChild(p1.getPoint(), p2.getPoint());
                child.normalize();
                if (point.equals(child)) {
                    removeEdgeBetween(p1, p2);
                }
            }
        }
    }

    private void removeEdgeBetween(DiscretePointDetailed dpd1, DiscretePointDetailed dpd2) {
        removeEdge(dpd1, dpd2);
        removeEdge(dpd2, dpd1);
    }

    private void removeEdge(DiscretePointDetailed dpd1, DiscretePointDetailed dpd2) {
        DiscretePoint dp2 = dpd2.getPoint();
        if (dpd1.getParents() != null) {
            for (int i = 0; i < dpd1.getParents().size(); i++) {
                DiscretePointDetailed parent = dpd1.getParents().get(i);
                if (parent.getPoint().equals(dp2)) {
                    dpd1.getParents().remove(i);
                    break;
                }
            }
        }
    }

    private DiscretePoint getChild(DiscretePoint p1, DiscretePoint p2) {
        int maxK = Math.max(p1.k, p2.k);
        p1 = p1.getNotNormalForm(maxK);
        p2 = p2.getNotNormalForm(maxK);
        return new DiscretePoint(p1.x + p2.x, p1.y + p2.y, maxK + 1);
    }

    private Map<DiscretePoint, List<DiscretePoint>> buildNeighbours() {
        Map<DiscretePoint, List<DiscretePoint>> result = new HashMap<>();

        for (DiscretePoint point : activePointsMap.keySet()) {
            result.put(point, new ArrayList<>());
        }

        for (Map.Entry<DiscretePoint, DiscretePointDetailed> entry : activePointsMap.entrySet()) {
            DiscretePoint currentPoint = entry.getKey();
            List<DiscretePoint> neighbours = result.get(entry.getKey());
            List<DiscretePointDetailed> parents = entry.getValue().getParents();
            if (parents != null) {
                neighbours.addAll(parents.stream().map(DiscretePointDetailed::getPoint).collect(Collectors.toList()));
                for (DiscretePointDetailed parent : parents) {
                    result.get(parent.getPoint()).add(currentPoint);
                }
            }
        }

        return result;
    }
}
