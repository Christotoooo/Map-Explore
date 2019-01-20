package cn.edu.pku.sei.drivers;

import cn.edu.pku.sei.Explore;
import cn.edu.pku.sei.TestLogger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class TsetExplore {

    //private static Explore app;
    public String fname = "/terrain.txt";

    

    @Test
    public void testMain() throws IOException, InterruptedException {
        String[] args = {fname};
        String expected = "[12, 13, 19, 18]";
        Explore.setLogger(new TestLogger());
        Explore.main(args);
        String result = Explore.getLogger().toString();
        assertEquals(expected,result);
    }
    
    /*//test main without argument
    @Test
    public void testFile() throws IOException, InterruptedException {
    	String[] args = {fname};
    	String expected = "[12, 13, 19, 18]";
        Explore.setLogger(new TestLogger());
        Explore.main(args);
        String result = Explore.getLogger().toString();
        assertEquals(expected,result);
    }*/


}
