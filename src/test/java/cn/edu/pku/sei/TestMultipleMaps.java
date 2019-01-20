package cn.edu.pku.sei;

import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.Coordinate.Pair;
import org.junit.Ignore;

import static org.junit.Assert.assertEquals;


import javax.swing.*;

import java.io.IOException;

public class TestMultipleMaps {
    public String fname0 = "/terrain.txt";
    public String fname1 = "/terrain_with_urban.txt";

    @Ignore
    public void test()throws IOException, InterruptedException{
        Pair size0 = new Pair(5,6,'T');
        Pair size1 = new Pair(7,7,'T');
    	String[] args0 = {fname0};
        Explore.main(args0);
        JFrame frame0 = Explore.app.nowFrame;
        frame0.setVisible(true);
        frame0.toFront();
        frame0.requestFocus();
        //System.out.println(frame0.isFocused());
        Pair size = Coordinate.getSize();
        assertEquals(size,size0);
        
        final String[] args1 = {fname1}; // focus changed!!!
        Explore.main(args1);
        JFrame frame1 = Explore.app.nowFrame;
        frame1.setVisible(true);
        frame1.toFront();
        frame1.requestFocus();
        size = Coordinate.getSize();
        assertEquals(size,size1);
    }
}
