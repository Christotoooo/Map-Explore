package cn.edu.pku.sei.mapStructures;

import cn.edu.pku.sei.rendering.Delta;
import java.awt.KeyboardFocusManager;


import java.util.HashMap;

public class Coordinate {
    
    //Constants related to board geometry
    private static int cols;
    private static int rows;
    private static HashMap<String,Integer> colsMap = new HashMap<>();//group 3
    private static HashMap<String,Integer> rowsMap = new HashMap<>();//group 3
    private static final int screenOrigX = 72;
    private static final int screenOrigY = -10;
    private static final int screenCellDx = 72 + 36;
    private static final int screenCellDy = 63;
    private static final int xCellCenterOffset = 72;
    private static final int yCellCenterOffset = 72;
    // getters
    public static int getXCellCenterOffset() {
    	return xCellCenterOffset;
    }
    public static int getYCellCenterOffset() {
    	return yCellCenterOffset;
    }
    
    public static final boolean RECT = true;
    
    //Cached values for linear, rectangular, board, 
    // and screen coordinates of this cell
    int index = 0;
    int xRect = 0;
    int yRect = 0; 
    int xHex  = 0;
    int yHex  = 0;
    int xCellOrigLoc = 0; 
    int yCellOrigLoc = 0;
    
    /**
     * This function or it's more complex override must be called
     * before any vertices are created and before any Coordinate instances
     * are created. This function is used to define the width and height of the 
     * terrain map/terrain graph. Without this information, it is impossible to 
     * correlate board cells with graph vertices. 
     * 
     * @param width
     * @param height
     */
    
    public boolean equals(Coordinate cc) {
    	return this.xHex == cc.getHexX() && this.yHex == cc.getHexY();
    }

    private static int cnt = 0;//group 3
    private static String curFrame;//group 3
    public static void setGeometry(int width, int height){//group 3
        String t;
        try {
            t = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow().getName();
        }catch (Exception e){
            t = "frame0";
        }
        if(t!="Maps"){
            cols = width;
            rows = height;
            colsMap.put(curFrame,width);
            rowsMap.put(curFrame,height);
        }
        cols = width;
        rows = height;
        colsMap.put("frame"+cnt,width);
        rowsMap.put("frame"+cnt,height);
        cnt += 1;
    }
    public static void changeGeometry(String windowName){//group 3
        System.out.println(windowName);
        cols = colsMap.get(windowName);
        rows = rowsMap.get(windowName);
        curFrame = windowName;
    }
    public static int getCnt(){
        return cnt;
    }//group 3
    
    public static final class Pair {
        public final int x;
        public final int y;
        public char  type;
        
        public Pair(int x, int y, char type){
            this.x = x;
            this.y = y; 
            this.type = type; 
        }
        
        @Override
        public String toString(){
            return String.format(" (%c:%d,%d)", type, x, y);
        }

        @Override
        public boolean equals(Object t){
            if(t instanceof Pair){
                Pair pt = (Pair) t;
                if(pt.x == x && pt.y == y && pt.type == pt.type){
                    return true;
                }
            }
            return false;
        }

    }

    public static Pair getSize(){
        return new Pair(cols,rows,'T');
    }

    
    /*public static void setGeometry(
            int x, 
            int y,
            int screenOffsetX,
            int screenOffsetY,
            int cellDx,
            int cellDy,
            int centerDx,
            int centerDy
            )
    {
       cols = x;
       rows = y;
       screenOrigX = screenOffsetX;
       screenOrigY = screenOffsetY;
       screenCellDx = cellDx;
       screenCellDy = cellDy;  
       xCellCenterOffset = centerDx;
       yCellCenterOffset = centerDy;
    };*/
    
    public Coordinate (int index){
        if (cols <= 0 || rows <= 0){
            throw new IllegalStateException(
               "Coordinate class dimensions must be set "
               + "before instancing.");
        }
        if (index >= rows * cols){
            throw new IllegalArgumentException(String.format(
               "Linear index %d is out of range for board geometer WxH %d %d%n",
                index, cols, rows)
            );
        }
        refresh(index);
    }
    
    public Coordinate (int row, int col, boolean rect){
       this(row*cols + col);
    }
    
    public Coordinate (int x, int y){
        refresh(indexFromBoard(x,y));
    }
    
    public Coordinate (Pair coord){
        if (!isValid(coord)){
            throw new IllegalArgumentException(
              "Pair "+coord+ "is not a valid coordinate. ");
        }
        switch (coord.type){
        case 'B': 
            refresh(indexFromBoard(coord.x, coord.y));
            break;
        case 'R': 
            refresh(indexFromRect(coord.x, coord.y));
            break;
        default:
            throw new IllegalArgumentException(
                    "Pair "+coord+ "is not a convertible coordinate. ");          
        }
    }

    private int indexFromRect(int x, int y) {
        return y*cols +x;
    }

    public static int indexFromBoard(int x, int y) {
        //adjust y to rect value
        int row = y - (x+1)/2;
        return cols*row + x;
    }

    private void refresh(int index){
        // set linear
        this.index = index;
        // compute rectangular
        xRect = index%cols;
        yRect = (index)/cols;
        // compute board coords
        xHex  = xRect;
        yHex  = yRect + (xRect+1)/2;
        // compute screen coords
        xCellOrigLoc = screenOrigX + (xRect * screenCellDx);
        yCellOrigLoc = screenOrigY + (yRect * 2 + xRect % 2) * screenCellDy;
    }
    
    //group 3 story 14
    public static HashMap<String, Integer> getHashRows(){
    	return rowsMap;
    }
    
    //group 3 story 14
    public static HashMap<String, Integer> getHashCols(){
    	return colsMap;
    }
    
    public int getLinear(){
        return index;
    }
    
    public int getHexX(){
        return xHex;
    }
    
    public int getHexY(){
        return yHex;
    }
    
    public Pair getRect(){
        return new Pair(xRect, yRect, 'R');
    }
    
    public Pair getBoard(){
        return new Pair(xHex, yHex, 'B');
    }
    
    public Pair getScreen(){
        return new Pair(xCellOrigLoc,yCellOrigLoc, 'S');
    }
    
    public Pair getScreenCenter(){
        return new Pair(xCellOrigLoc+xCellCenterOffset,
                yCellOrigLoc+yCellCenterOffset, 'C');
    }
    
    public Coordinate get(HexDir dir){
        try {
            Delta delta = dir.getDelta();
            Pair  txlate = new Pair(xHex + delta.x, yHex + delta.y, 'B');
            if (txlate.x <0 || txlate.y < 0) return null;
            return  new Coordinate(txlate);  
        } catch (Exception e){
            return null; 
        }
    }
    
    
    public static boolean isValid(Pair coord) {
        if (coord.x < 0 || coord.x >= cols || coord.y < 0) return false;
        Pair rect = convertToRect(coord);
        if (rect == null) return false;
        if (rect.x <0 || rect.y < 0 ) return false;
        if (rect.x >= cols || rect.y >= rows) return false;
        return true;
    }

    public static Pair convertToRect(Pair cx) {
        switch(cx.type){
            case 'R': return cx;
            case 'B': 
                int ndx = indexFromBoard(cx.x, cx.y);
                return new Pair(ndx%cols, ndx/cols, 'R');
            default : return null; 
        }
    }

    public static void checkGeometry(int width, int height) {
        if (width != cols || height != rows)
            throw new IllegalStateException(String.format(
                    "Coordinate wxh = %d %d, called with %d %d%n",
                    cols, rows, width, height));
        
    }

    /**
     * @param c will be interpreted as a board coordinate
     * @return true if coord c is immediately adjcaent to this coord. 
     */
    public boolean isNeighbor(Coordinate c) {
        Pair dt = getDelta(c);
        return isNeighbor(dt);
    }
    
    /**
     * @param n will be interpreted as a board coordinate
     * @return true if coord c is immediately adjcaent to this coord. 
     */
    public boolean isNeighbor(Pair dt) {
        return ! (dt.x > 1 || dt.x < -1 || dt.y >1 || dt.y < -1);
    }
    
    /**
     * @param c will be interpreted as a board coordinate. 
     * @return the difference between this Coordinate and c, 
     * expressed as a vector in board coordinates. 
     */
    public Pair getDelta(Coordinate c){
        return new Pair(xHex - c.xHex, yHex - c.yHex, 'B' );
    }
}
