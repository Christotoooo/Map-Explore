package cn.edu.pku.sei.rendering;

public interface BoardCell extends HexGridCell, Cloneable {
    int getHexX();
    int getHexY();
    void setHexX(int x);
    void setHexY(int y);
    Object clone();
}
