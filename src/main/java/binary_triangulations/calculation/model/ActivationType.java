package binary_triangulations.calculation.model;


public enum ActivationType {
    //activating parents that lays diagonally (used when point is in the center of grid cell)
    FIRST,
    //activating parents that lays vertically/horizontally (used when point is on the bound of grid cell)
    SECOND
}