package cn.edu.pku.sei;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.util.DirectoryProbe;
import cn.edu.pku.sei.util.NoiseFilterReader;
@RunWith(Parameterized.class)

public class TestMapName {
	//test the map file name in story 9
	private String input;
	private String expected;	
	public TestMapName(String input, String expected) {
		 this.input = input;
		 this.expected = expected;
	}	

	@Parameterized.Parameters
	public static Collection<Object[]> fileMapName() {
	      return Arrays.asList(new Object[][] {
	         { "terrain a 5 6 withname.txt", "PKU" },
	         { "terrain b 5 6 withname.txt", "THU"},
	         { "terrain withname.txt", "Beijing" },
	         { "terrain a 5 6 .txt", "noname" },
	         { "terrain b 5 6 .txt", "noname" },
	         {"terrain_with_urban.txt","noname"},
	         {"terrain_with_name_in_the_mid.txt","Shanghai"},
	         {"terrain_with_airport.txt","noname"}
	      });
	}
	
	
	@Test
	public void testMapName() throws ParseErrorException{
		String mapsloc = DirectoryProbe.probe("maps", "src/maps");
		FileReader fRdr = null;
		try {
			fRdr = new FileReader(new File(mapsloc,input));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NoiseFilterReader rdr = new NoiseFilterReader(fRdr);
		TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        //System.out.println(input);
        //System.out.println(tGraph.name);
        //System.out.println(expected);
        assertEquals(tGraph.name,expected);
        //System.out.println(tGraph.toString());
	}
}
