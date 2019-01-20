package cn.edu.pku.sei;

import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.rendering.TerrainBoard;
import cn.edu.pku.sei.rendering.TerrainGraph;

/**
 * 
 * @author Group 3 for Story #57
 * 
 * a problem: when the theme in the option menu changed, mouse clicks cannot be heard anymore.
 * 			this is caused by creating a new TerrainBoard object in line 168 in Explore.java,
 * 			and is solved by adding a line of addMouseListner in line 169 in Explore.java.
 * 
 */

public class GraphMouseListener implements MouseListener {
	static boolean ignoreMouse = false;

	public static final class Pair {
        public final int x;
        public final int y;
        
        public Pair(int x, int y){
            this.x = x;
            this.y = y;
        }
        public double distanceTo(Pair other) {
        	int dx = x - other.x;
        	int dy = y - other.y;
        	return Math.sqrt(dx*dx + dy*dy);
        }
        
        @Override
        public String toString(){
            return String.format("(%d,%d)", x, y);
        }
        @Override
        public boolean equals(Object obj) {
        	if (obj == null) {
    			return false;
    		}
    		if (obj == this) {
    			return true;
    		}
    		if (!(obj instanceof Pair)) {
    			return false;
    		}
    		Pair other = (Pair) obj;
    		return other.toString().equals(this.toString());
        }
        @Override
        public int hashCode() {
        	return this.toString().hashCode();
        }
    }
	
	Map<Pair, Pair> coord2cell = new HashMap<>();
	TerrainGraph tGraph = null;
	GraphMouseListener(TerrainBoard m) {
		tGraph = m.getGraph();
		final int xCellCenterOffset = Coordinate.getXCellCenterOffset(); //coherent with Coordinate.java
		final int yCellCenterOffset = Coordinate.getYCellCenterOffset();
		// record coordinates of every cell's center
		for (int row = 0; row < m.getBoardy(); row++) {
            for (int col = 0; col < m.getBoardx(); col++) {
                int locx = m.getOriginalX() + (col * m.getDx());
                int locy = m.getOriginalY() + ((row * 2 + (col % 2)) * m.getDy());
                //String cell = "(" + (col) + "," + (row + (col + 1) / 2) + ")";
                coord2cell.put(new Pair(locx+xCellCenterOffset, locy+yCellCenterOffset), new Pair(col,(row + (col + 1) / 2)));
            }
		}
	}

	JFrame jf;
    @Override
    public void mouseClicked(MouseEvent e) {
		// TODO when click, pop up a dialog window
    	System.out.println("mouseClicked!");
        //System.out.println("click at:("+e.getX()+","+e.getY()+")\n");
    	Pair click = new Pair(e.getX(), e.getY());
    	// calculate the nearest cell center to decide which cell is clicked
    	Pair cell = null;
    	double minn = 72; //radius of a cell
        for (Pair coord:coord2cell.keySet()) {
        	double distance = click.distanceTo(coord);
        	if (distance < minn) {
        		minn = distance;
        		cell = coord2cell.get(coord);
        	}
        }
        // if every distanceTo() > radius, that means click coordinates not inside a cell.   
        if (cell != null) {
        	if(e.getButton()==e.BUTTON1) 
        	{
        		if(jf != null) {
            		//when there is already a jf window, we need it to be closed automatically before next jf window is generated
            		jf.dispose();
            	}
            	System.out.println("Cell = " + cell.toString());
            	
            	//jf = new JFrame();
            	//jf.setTitle("information");
            	
            	//TODO display information of cells in the jf window. Need to locate the cell information in the map file.
            	String cellDescription = tGraph.getVertex(cell.x,cell.y).getData().getTerrain().getDescription();
				JOptionPane.showMessageDialog(null, cellDescription);
				//jf.getContentPane().add(new JLabel(cellDescription));
            	
            	//jf.setSize(300, 400);
            	//jf.setVisible(true);
        	}
        	if(e.getButton()==e.BUTTON3)  //Group1,story58
        	{
        		PopHelppage.popPage("src/helper/index2.html");
        	}
        	
        }
        else
        {
        	if(e.getButton()==e.BUTTON3) PopHelppage.popPage("src/helper/index3.html");
        }
    }
    
	@Override
    public void mousePressed(MouseEvent e) {
        // TODO  
		//System.out.println("mousePressed!");
        //System.out.println("click at:("+e.getX()+","+e.getY()+")\n");
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO 
		//System.out.println("mouseReleased!");
        //System.out.println("click at:("+e.getX()+","+e.getY()+")\n");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    	if(ignoreMouse) return;
        // TODO 
		System.out.println("mouseEntered!");
		String focusWindow = ((TerrainBoard)e.getSource()).getRootPane().getParent().getName();
		Coordinate.changeGeometry(focusWindow);
        //System.out.println("click at:("+e.getX()+","+e.getY()+")\n");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO 
		//System.out.println("mouseExited!");
        //System.out.println("click at:("+e.getX()+","+e.getY()+")\n");
    }
    
    public void mouseDoubleClicked(MouseEvent e) {
    	Pair click = new Pair(e.getX(), e.getY());
    	// calculate the nearest cell center to decide which cell is clicked
    	Pair cell = null;
    	double minn = 72; //radius of a cell
        for (Pair coord:coord2cell.keySet()) {
        	double distance = click.distanceTo(coord);
        	if (distance < minn) {
        		minn = distance;
        		cell = coord2cell.get(coord);
        	}
        }
        // if every distanceTo() > radius, that means click coordinates not inside a cell.   
        if (cell != null) {
        	if(e.getButton()==e.BUTTON1) 
        	{
        		if(jf != null) {
            		//when there is already a jf window, we need it to be closed automatically before next jf window is generated
            		jf.dispose();
            	}
            	System.out.println("Cell = " + cell.toString());
            	
            	//jf = new JFrame();
            	//jf.setTitle("information");
            	
            	//TODO display information of cells in the jf window. Need to locate the cell information in the map file.
            	String cellDescription = tGraph.getVertex(cell.x,cell.y).getData().getTerrain().getDescription();
				JOptionPane.showMessageDialog(null, cellDescription);
				//jf.getContentPane().add(new JLabel(cellDescription));
            	
            	//jf.setSize(300, 400);
            	//jf.setVisible(true);
        	}
        	if(e.getButton()==e.BUTTON3)  //Group1,story58
        	{
        		PopHelppage.popPage("src/helper/index2.html");
        	}
        	
        }
        else
        {
        	if(e.getButton()==e.BUTTON3) PopHelppage.popPage("src/helper/index3.html");
        }
    	
    }


}
