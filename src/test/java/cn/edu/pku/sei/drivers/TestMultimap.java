package cn.edu.pku.sei.drivers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import cn.edu.pku.sei.Explore;
import cn.edu.pku.sei.MapSelection;
import cn.edu.pku.sei.TestLogger;
import cn.edu.pku.sei.mapStructures.Coordinate;

public class TestMultimap {

	private String[] mapName = {"terrain_with_airport 3.txt", "terrain a 5 6 .txt", "terrain_with_urban.txt"};
	
	//run this test before all other tests, ensuring the hashmap is as expected
	@Ignore
	public void test() throws IOException, InterruptedException {
		
		MapSelection.openedGraph = 0;
		Coordinate.getHashCols().clear();
		Coordinate.getHashRows().clear();
		
		Explore.setLogger(new TestLogger()); 
		HashMap<String, Integer> expectCols = new HashMap<String, Integer>();
		HashMap<String, Integer> expectRows = new HashMap<String, Integer>();
		expectCols.put("frame" + Coordinate.getCnt(), 7); expectRows.put("frame" + Coordinate.getCnt(), 7);
		expectCols.put("frame" + (Coordinate.getCnt() + 1), 5); expectRows.put("frame" + (Coordinate.getCnt() + 1), 6);
		expectCols.put("frame" + (Coordinate.getCnt() + 2), 7); expectRows.put("frame" + (Coordinate.getCnt() + 2), 7);
		for(int i = 0; i < 3; ++i) {
			Explore.main(new String[] {mapName[i]});
		}
		
		HashMap<String, Integer> resultCols = Coordinate.getHashCols();
		HashMap<String, Integer> resultRows = Coordinate.getHashRows();
		
		System.out.println("===================");
		System.out.println(expectCols);
		System.out.println(expectRows);
		System.out.println(resultCols);
		System.out.println(resultRows);
		System.out.println("===================");

		
		assertEquals(resultRows, expectRows);
		assertEquals(resultCols, expectCols);
	}

}
