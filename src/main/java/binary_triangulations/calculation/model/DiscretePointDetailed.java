package binary_triangulations.calculation.model;

import binary_triangulations.calculation.model.basic.DiscretePoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class DiscretePointDetailed {

    private DiscretePoint point;

    private ArrayList<DiscretePointDetailed> parents;

    public DiscretePointDetailed(int x, int y, int k, ArrayList<DiscretePointDetailed> parents) {
        this.point = new DiscretePoint(x, y, k);
        this.parents = parents;
    }
}
