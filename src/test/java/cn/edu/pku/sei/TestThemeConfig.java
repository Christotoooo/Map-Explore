package cn.edu.pku.sei;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.util.NoiseFilterReader;
import org.junit.Test;

import cn.edu.pku.sei.rendering.ImageCache;
import cn.edu.pku.sei.mapStructures.TerrainType;
/*
 * Theme Config Parser Test
 * 
 *   -- group 1
 */

public class TestThemeConfig {

    @Test 
    public void testConfig() throws  IOException, ParseErrorException{
    	System.out.println("Theme name test start!");

		String file = "src/maps/terrain_for_testing_theme_config.txt";

		FileReader fRdr = new FileReader(new File(file));
		NoiseFilterReader rdr = new NoiseFilterReader(fRdr);

		TerrainLoader tr = new TerrainLoader(rdr);
		TerrainGraph graph =  tr.load();

    	ImageCache ic = new ImageCache(graph.getThemeName());
    	
    	System.out.println("ImageCache constructed!");
    	
    	String conf_path = "src/config/theme.conf";
    	File conf_file = new File(conf_path);
    	String theme_name = graph.getThemeName();
    	String theme_conf_path = ic.getThemeConfigPath();
    	try {
    		InputStreamReader reader = new InputStreamReader(
    				new FileInputStream(conf_file));
    		BufferedReader br = new BufferedReader(reader);
    		boolean flag = true;
    		String line = br.readLine();
    		System.out.println("Theme name: "+theme_name);
    		System.out.println("Theme config: "+theme_conf_path);
    		System.out.println("Content in "+conf_path+":");
    		while(line != null) {
    			System.out.println(line);
    			if (line.trim().length() <= 0 || line.charAt(0) == '#') {
    				line = br.readLine();
    				continue;
    			}
    			if ((!line.contains(theme_name)) && line.contains("THEME") && line.contains("=")) {
					System.out.println("Wrong theme name detected!");
    				flag = false;
				}
    			else if (line.contains(theme_name) && (!line.contains("=")) && (!line.contains(theme_conf_path))) {
    				System.out.println("Wrong theme configuration file detected!");
    				flag = false;
    			}
//    			System.out.println("checkpoint");
    			line = br.readLine();
    		}
    		assertEquals(flag, true);
    		br.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	System.out.println("Theme name test done!");
    }
    
    @Test
    public void testThemeConfig() throws IOException, ParseErrorException{
    	System.out.println("Theme config test start!");

		String file = "src/maps/terrain_for_testing_theme_config.txt";

		FileReader fRdr = new FileReader(new File(file));
		NoiseFilterReader rdr = new NoiseFilterReader(fRdr);

		TerrainLoader tr = new TerrainLoader(rdr);
		TerrainGraph graph =  tr.load();

		ImageCache ic = new ImageCache(graph.getThemeName());


		System.out.println("ImageCache constructed!");
    	
    	String theme_name = graph.getThemeName();
    	String theme_conf_path = ic.getThemeConfigPath();
    	File theme_conf_file = new File(theme_conf_path);
    	ImageCache.FileAssn[] bg = ic.getBgAssn();
    	ImageCache.FileAssn[] txp = ic.getTxpAssn();
    	System.out.println("Theme name: "+theme_name);
		System.out.println("Theme config: "+theme_conf_path);
    	ArrayList<String> a = new ArrayList<>();
    	
    	// ADD THE NAME HERE, PLEASE!!!
    	// IN THE CORRECT ORDER, PLEASE!!!
    	String[] bgString = {"brush", "water", "forest", "flag1", "flag2", "urban", "airport"};
        String[] txpString = {"wall", "dirt", "hwy2", "hwy4", "path",  "river",  "bridge", "runway", "mountain"};
    	
        System.out.println("Image paths sotred in ImageCache:");
    	for (int i = 0; i < txp.length; i++) {
    		System.out.println(txpString[i]+"\t"+txp[i].getFname());
    	}
    	for (int i = 0; i < bg.length; i++) {
    		System.out.println(bgString[i]+"\t"+bg[i].getFname());
    	}
    	try {
    		InputStreamReader reader = new InputStreamReader(
    				new FileInputStream(theme_conf_file));
    		BufferedReader br = new BufferedReader(reader);
    		String line = br.readLine();
    		System.out.println("Content in "+theme_conf_path+":");
    		while(line != null) {
    			if (line.trim().length() <= 0 || line.charAt(0) == '#') {
    				line = br.readLine();
    				continue;
    			}
    			System.out.println(line);
    			a.add(line);
    			line = br.readLine();
    		}
    		br.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	for (String s: a) {
    		boolean flag = true;
    		for (int i = 0; i < bg.length; i++) {
    			if (s.contains(bg[i].getFname()) && s.contains(bgString[i])) {
    				flag = false;
    				break;
    			}
    		}
    		for (int i = 0; i < txp.length; i++) {
    			if (s.contains(txp[i].getFname()) && s.contains(txpString[i])) {
    				flag = false;
    				break;
    			}
    		}
    		if (flag) {
    			System.out.println("The path of "+s+"is wrong!");
    		}
    		assertEquals(flag, false);
    	}
    	System.out.println("Theme config test done!");
	}
    
    @Test
    public void testThemeConfigAuthorName() throws IOException, ParseErrorException{
    	System.out.println("Theme author test start!");

		String file = "src/maps/terrain_for_testing_theme_config.txt";

		FileReader fRdr = new FileReader(new File(file));
		NoiseFilterReader rdr = new NoiseFilterReader(fRdr);

		TerrainLoader tr = new TerrainLoader(rdr);
		TerrainGraph graph =  tr.load();

		ImageCache ic = new ImageCache(graph.getThemeName());


		System.out.println("ImageCache constructed!");
    	
    	String theme_name = graph.getThemeName();
    	String theme_conf_path = ic.getThemeConfigPath();

    	File theme_conf_file = new File(theme_conf_path);
    	System.out.println("Theme name: "+theme_name);
		System.out.println("Theme config: "+theme_conf_path);
    	ArrayList<String> a = new ArrayList<>();
    	
    	// ADD THE NAME HERE, PLEASE!!!
    	// IN THE CORRECT ORDER, PLEASE!!!
    	String[] bgString = {"brush", "water", "forest", "flag1",
    						 "flag2", "urban", "airport"};
    	TerrainType[] bgType = {TerrainType.kBrush, TerrainType.kWater,
    			TerrainType.kForest, TerrainType.kFlag1,
    			TerrainType.kFlag2, TerrainType.kUrban,
                TerrainType.kAirport };
    	String[] txpString = {"wall", "dirt", "hwy2", "hwy4", "path", 
    						  "river",  "bridge", "runway", "mountain"};
    	
        System.out.println("Image authors sotred in ImageCache:");
    	for (int i = 0; i < bgString.length; i++) {
    		System.out.println(bgString[i]+"\t"
    				+ic.getTerrainAuthor(bgType[i]));
    	}
    	try {
    		InputStreamReader reader = new InputStreamReader(
    				new FileInputStream(theme_conf_file));
    		BufferedReader br = new BufferedReader(reader);
    		String line = br.readLine();
    		System.out.println("Content in "+theme_conf_path+":");
    		while(line != null) {
    			if (line.trim().length() <= 0 || line.charAt(0) == '#') {
    				line = br.readLine();
    				continue;
    			}
    			boolean flag = false;
    			for (String s : txpString) {
    				if (line.contains(s)) {
    					flag = true;
    					break;
    				}
    			}
    			if (flag) {
    				line = br.readLine();
    				continue;
    			}
    			System.out.println(line);
    			a.add(line);
    			line = br.readLine();
    		}
    		br.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	for (String s: a) {
    		boolean flag = true;
    		for (int i = 0; i < bgString.length; i++) {
    			String tmp_author = ic.getTerrainAuthor(bgType[i]);
    			String tmp_name = bgString[i];
    			System.out.println("\t\t"+tmp_author+"\t"+tmp_name+"\t"+s);
    			if (!s.contains(tmp_name)) {
					continue;
				}
    			if (tmp_author == null) {
    				flag = false;
    				break;
    			}
    			String[] tmp_name_sub = tmp_name.split(" |\t");
    			boolean tmp_flag = true;
    			for (String tmp_string : tmp_name_sub) {
    				if (!s.contains(tmp_string)) {
    					tmp_flag = false;
    					break;
    				}
    			}
    			if (tmp_flag) {
    				flag = false;
    				break;
    			}
    		}
    		if (flag) {
    			System.out.println("The author of "+s+"is wrong!");
    		}
    		assertEquals(flag, false);
    	}
    	System.out.println("Theme author test done!");
	}

}
