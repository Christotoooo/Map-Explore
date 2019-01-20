package cn.edu.pku.sei.rendering;

import cn.edu.pku.sei.mapStructures.Coordinate;

public interface HexGridCell extends Cloneable {
    Coordinate getCoordinates();
    void setCoordinate(Coordinate ycx);
    void setCoordinate(int index);
    Object clone();

}
