package cn.edu.pku.sei;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import cn.edu.pku.sei.mapStructures.ConnType;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.mapStructures.Path;
import cn.edu.pku.sei.mapStructures.TerrainType;
import cn.edu.pku.sei.rendering.TerrainCell;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.shared.Graph.Edge;
import cn.edu.pku.sei.shared.Graph.Vertex;
import cn.edu.pku.sei.util.DirectoryProbe;
import cn.edu.pku.sei.util.NoiseFilterReader;

public class TestGraph {

    @Test
    public void test() {
       Coordinate.setGeometry(4, 4);
       TerrainGraph g = new TerrainGraph(4, 4);
       /*
        * Test Terrain Type!
        * 	-- group 1
        */
       g.setTerrain(0, 2, TerrainType.kUrban);
       g.setTerrain(0, 2, TerrainType.kUrban);
       g.setTerrain(0, 1, TerrainType.kBrush);
       g.setTerrain(1, 2, TerrainType.kForest);
       g.setTerrain(1, 3, TerrainType.kUrban);
       g.setTerrain(0, 3, TerrainType.kWater);
       /*
        * END!
        * 	-- group 1
        */
       Vertex<TerrainCell> v = g.getVertex(0,2);
       assertNotNull(v);
       assertNotNull(v.getEdges());
       assertEquals(8, v.getIndex());
       System.out.println(v); 
       System.out.println();
       System.out.println(g);
       
       List<Edge> buffer = new ArrayList<Edge>();
       for (Edge x: v.getEdges()){
           buffer.add(x);
       }
       System.out.println(buffer);
       buffer.sort(new Comparator<Edge>() {

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.getTo() - o2.getTo();
        }       
           
       });
       System.out.println(buffer.toString());
       System.out.println("[(8, 4: 180) , (8, 5: 195) , (8, 9: 100) , (8, 12: 190) ]");
       
       

       assertEquals("[(8, 4: 180) , (8, 5: 195) , (8, 9: 100) , (8, 12: 190) ]", buffer.toString());
//       assertTrue(g.toString().contains("10: (10, 11: 165) (10, 5: 165) (10, 14: 165) (10, 6: 165) (10, 9: 165) (10, 7: 165) "));
       
    
    }
    
    @Test
    public void testAirport() {
       Coordinate.setGeometry(4, 4);
       TerrainGraph g = new TerrainGraph(4, 4);
       g.setTerrain(0, 2, TerrainType.kAirport);
       g.setTerrain(0, 1, TerrainType.kAirport);
       try {
    	   g.setConnectedPath(new Path(new Coordinate(0,2), new Coordinate(0,1)), ConnType.runway);
       } catch (Exception e) {
    	   
       }
       
       Vertex<TerrainCell> v = g.getVertex(0,2);
       assertNotNull(v);
       assertNotNull(v.getEdges());
       assertEquals(8, v.getIndex());
       System.out.println(v); 
       System.out.println();
       System.out.println(g);
       
       List<Edge> buffer = new ArrayList<Edge>();
       for (Edge x: v.getEdges()){
           buffer.add(x);
       }
       System.out.println(buffer);
       buffer.sort(new Comparator<Edge>() {

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.getTo() - o2.getTo();
        }       
           
       });
       System.out.println("buffer to stirng = "+ buffer.toString());
       System.out.println("[(8, 4: 105) , (8, 5: 165) , (8, 9: 165) , (8, 12: 165) ]");
       

       assertEquals("[(8, 4: 105) , (8, 5: 165) , (8, 9: 165) , (8, 12: 165) ]", buffer.toString());
      
    }
    
    @Test
    public void TestAirport()throws ParseErrorException{
    	String mapsloc = DirectoryProbe.probe("maps", "src/maps");
		FileReader fRdr = null;
		String input="terrain_with_airport.txt";
		int airportx1=0;
		int airporty1=0;
		int airportx2=0;
		int airporty2=1;
		try {
			fRdr = new FileReader(new File(mapsloc,input));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NoiseFilterReader rdr = new NoiseFilterReader(fRdr);
		TerrainLoader loader = new TerrainLoader(rdr);       
		TerrainGraph tGraph = loader.load();
		System.out.println(tGraph.toString());
		TerrainType cellType = tGraph.getVertex(airportx1,airporty1).getData().getTerrain();
		assertEquals(cellType.getNumVal(),105);
		TerrainType cellType1 = tGraph.getVertex(airportx2,airporty2).getData().getTerrain();
		assertEquals(cellType1.getNumVal(),105);
    }
}
