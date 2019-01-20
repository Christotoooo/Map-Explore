package cn.edu.pku.sei.drivers;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import cn.edu.pku.sei.TerrainLoader;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.util.DirectoryProbe;
import cn.edu.pku.sei.util.NoiseFilterReader;



public class testtoString {
	
	 /**
     * Description: test if the output of ttoString method is stable
     * @author Group 5 Story #64
     */
	
	@Test
    public void testToStringFromFile() throws ParseErrorException, IOException{
		String file = "terrain_save_result.txt";
		String mapsloc = DirectoryProbe.probe("maps", "src/maps");
	     if (mapsloc == null){
	            throw new FileNotFoundException("Cannot find maps directory.");
	     }
	     FileReader fRdr = new FileReader(new File(mapsloc,file));
	     Reader rdr = new NoiseFilterReader(fRdr); 
	     TerrainLoader loader = new TerrainLoader(rdr);
	     TerrainGraph tGraph = loader.load();
        

        double r= checkStability(100,tGraph); //run 100 times
        System.out.println("result="+r);
        
        double expected=1.00;
        
   
        assertEquals(expected,r,0.0);

    }
	
	

	
	//return the probability of a stable output of toString
	public double checkStability(int runNtimes, TerrainGraph g)  {
		String previousResult,result;
		int count=0; 
		previousResult=g.toString();
		for (int i=0;i<runNtimes;i++) {   
			 result=g.toString();
			 if(result.equals(previousResult)) {       	    		
//        		System.out.println("loop"+i+":");
//				System.out.println(previousResult);
//        		System.out.println("---------------------------");
//        		System.out.println(result);
        		//assertEquals(result,previousResult);
        		count+=1;
        	}
        	
          	previousResult=result;
        }
		
		System.out.println(count);
		return (double)(count/runNtimes);
		
	}
	
}
