
package cn.edu.pku.sei.rendering;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import cn.edu.pku.sei.mapStructures.ConnType;
import cn.edu.pku.sei.mapStructures.TerrainType;

public class ImageCache {
    
    Map<Enum, Map<Integer,BufferedImage>> cache
        = new HashMap<Enum, Map<Integer, BufferedImage>>();

    Map<TerrainType, BufferedImage> tCache = new HashMap<TerrainType, BufferedImage>();
    Map<TerrainType, String> tAuthorCache = new HashMap<TerrainType, String>();
    
    private String imageFolder ="./images/";
    private String altImageLoc = "./src/images/";
    private String themeConfigPath = "";
    
    public BufferedImage hexTile;
    
    public  class FileAssn {
        
        private String fname;
        private Enum type;

        public FileAssn(String fname, TerrainType type){
           this.fname = fname;
           this.type  = type;
        }
        
        public FileAssn(String fname, ConnType type){
           this.fname = fname;
           this.type  = type;
        }
        
        public String getFname() {
        	return fname;
        }
    }
    
    // Please change line 90 & 159 in TestThemeConfig.java.
    // 	When you add a new item, please add its name in bgString.
     FileAssn[] bgAssn = {
            // using weight for selector
            new FileAssn("bg_brush.png", TerrainType.kBrush),
            new FileAssn("bg_water.png", TerrainType.kWater),
            new FileAssn("bg_forrest.png", TerrainType.kForest),
            new FileAssn("bg_flag1.png", TerrainType.kFlag1),
            new FileAssn("bg_flag2.png", TerrainType.kFlag2),
            new FileAssn("bg_urban.png", TerrainType.kUrban),
            new FileAssn("bg_airport.png", TerrainType.kAirport)
    };

    
    // Please change line 91 in TestThemeConfig.java.
    // 	When you add a new item, please add its name in txpString.
     FileAssn[] txpAssn = {
            // using weight for transport cost
            new FileAssn("wall.png", ConnType.wall),
            new FileAssn("dirt.png", ConnType.dirt),
            new FileAssn("hwy2.png", ConnType.hwy2),
            new FileAssn("hwy4.png", ConnType.hwy4),
            new FileAssn("path.png", ConnType.path),
            new FileAssn("river.png", ConnType.river),
            new FileAssn("bridge.png",ConnType.bridge),
            new FileAssn("runway.png", ConnType.runway),
	    new FileAssn("mountain.png", ConnType.mountain)
            
    };
       


    
    public ImageCache(String themeName){

    	if(themeName == null || themeName.length() == 0)
		{
			themeName =  "default";
		}


        File cwd = new File(".");
        File dirProbed = new File(imageFolder);
        
        /*
         * Theme configuration parsing & setting
         * And author signature
         * 	-- group 1
         */
        String conf_path = "src/config/theme.conf";
    	File conf_file = new File(conf_path);
    	String theme_conf_path = null;
    	File theme_conf_file = null;
    	
    	try {
    		InputStreamReader reader = new InputStreamReader(
    				new FileInputStream(conf_file));
    		BufferedReader br = new BufferedReader(reader);
    		String line = br.readLine();
    		while(line != null) {
//    			System.out.println(line);
    			if(theme_conf_path != null) {
    				break;
    			}
    			if (line.length() <= 0 || line.charAt(0) == '#'){
    				line = br.readLine();
    				continue;
    			}
    			if (!line.contains(themeName)) {
    				line = br.readLine();
    				continue;
    			}
    			theme_conf_path = line.substring(line.indexOf(themeName)+themeName.length()).trim();
    			//break;
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    		System.out.println("Theme configuration file missing. ");
    		System.out.println("Use default theme setting...");
    		System.out.println("  The configuration file should be in "+conf_path);
    	}
		themeConfigPath = theme_conf_path;
//    	System.out.println(theme_name);
//    	System.out.println(theme_conf_path);
    	theme_conf_file = new File(theme_conf_path);
    	try {
    		InputStreamReader reader = new InputStreamReader(
    				new FileInputStream(theme_conf_file));
    		BufferedReader br = new BufferedReader(reader);
    		String line = br.readLine();
    		while(line != null) {
//    			System.out.println(line);
    			if (line.trim().length() <= 0 || line.charAt(0) == '#'){
    				line = br.readLine();
    				continue;
    			}
    			String[] tokens = line.split(" |\t");
    			String author;
    			if (tokens.length > 2) {
    				author = tokens[2].trim();
    			}
    			else {
    				author = "";
    			}
    			if (author .equals("<NONE>") ) {
    				author = null;
    			}
    			if (author != "") {
		    		for (int i = 3; i < tokens.length; i++) {
		   				author += " " + tokens[i].trim();
		   			}
    			}
    			line = br.readLine();
    			switch(tokens[0]) {
    				case "brush":
    					bgAssn[0] = new FileAssn(tokens[1], TerrainType.kBrush);
    					tAuthorCache.put(TerrainType.kBrush, author);
    					System.out.println("brush author: "+author);
    					break;
    				case "water":
    					bgAssn[1] = new FileAssn(tokens[1], TerrainType.kWater);
    					tAuthorCache.put(TerrainType.kWater, author);
    					System.out.println("water author: "+author);
    					break;
    				case "forest":
    					bgAssn[2] = new FileAssn(tokens[1], TerrainType.kForest);
    					tAuthorCache.put(TerrainType.kForest, author);
    					System.out.println("forest author: "+author);
    					break;
    				case "urban":
    					bgAssn[5] = new FileAssn(tokens[1], TerrainType.kUrban);
    					tAuthorCache.put(TerrainType.kUrban, author);
    					System.out.println("urban author: "+author);
    					break;
    				case "airport":
    					bgAssn[6] = new FileAssn(tokens[1], TerrainType.kAirport);
    					break;
    				case "flag1":
    					bgAssn[3] = new FileAssn(tokens[1], TerrainType.kFlag1);
    					tAuthorCache.put(TerrainType.kFlag1, author);
    					System.out.println("flag1 author: "+author);
    					break;
    				case "flag2":
    					bgAssn[4] = new FileAssn(tokens[1], TerrainType.kFlag2);
    					tAuthorCache.put(TerrainType.kFlag2, author);
    					System.out.println("flag2 author: "+author);
    					break;
    				case "wall":
    					txpAssn[0] = new FileAssn(tokens[1], ConnType.wall);
    					break;
    				case "dirt":
    					txpAssn[1] = new FileAssn(tokens[1], ConnType.dirt);
    					break;
    				case "hwy2":
    					txpAssn[2] = new FileAssn(tokens[1], ConnType.hwy2);
    					break;
    				case "hwy3":
    					txpAssn[3] = new FileAssn(tokens[1], ConnType.hwy4);
    					break;
    				case "hwy4":
    					txpAssn[3] = new FileAssn(tokens[1], ConnType.hwy4);
    					break;
    				case "path":
    					txpAssn[4] = new FileAssn(tokens[1], ConnType.path);
    					break;
    				case "river":
    					txpAssn[5] = new FileAssn(tokens[1], ConnType.river);
    					break;
    				case "runway":
    					txpAssn[6] = new FileAssn(tokens[1], ConnType.runway);
    				default:
    					System.out.println("Cannot resolve type \""+tokens[0]+"\" in the theme configuration file.");
    					System.out.println("Please check your input");
    					break;
    			}
    		}
    		br.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    		System.out.println("Theme configuration file missing. ");
    		System.out.println("Use default theme setting...");
    		System.out.println("  The configuration file should be in "+theme_conf_path);
    	}
    	/*
    	 * END!
    	 *   -- group 1
    	 */
    	
        if (!dirProbed.exists()){
            // try the alternate
            dirProbed = new File(altImageLoc);
            if (dirProbed.exists()) imageFolder = altImageLoc;
        }
        System.out.format("Expecting image folder at %s%s%n",
                cwd.getAbsolutePath(),imageFolder);
        
        hexTile = get(imageFolder+"hexgrid_template.png");
        // complete the multimap
        for (int i = 0; i < bgAssn.length; i++){
            BufferedImage bgImg = get(imageFolder+bgAssn[i].fname);
            if (bgImg != null)
                tCache.put((TerrainType) bgAssn[i].type, bgImg);
        }
        for (int i = 0; i < txpAssn.length; i++){
            BufferedImage img = get(imageFolder+txpAssn[i].fname);
            if (img != null)
                cacheRotations( img, (ConnType) txpAssn[i].type);
        }
    }
    
    public BufferedImage getHexTile(){
        return hexTile;
    }
    
    private void put(Enum type, Integer rAngle, BufferedImage img){
        Map<Integer, BufferedImage> group = null; 
        if ((group = cache.get(type)) == null){
            group = new HashMap<Integer, BufferedImage>();
            cache.put(type, group);
        }
        group.put(rAngle, img);
    }
    
    private void cacheRotations(BufferedImage img, ConnType type){
        put(type, 60, rotate(img, 60));
        put(type, 0, img);
        put(type, -60,rotate(img,-60));
        put(type, 120, rotate(img, 120));
        put(type, 180, rotate(img, 180));
        put(type, -120,rotate(img,-120));
    }
    
    private BufferedImage rotate(BufferedImage img, int angle){
        AffineTransform tx = AffineTransform.getRotateInstance(
                Math.toRadians(angle),
                71,71
                );
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(img, null);             
    }

    public BufferedImage getTerrain(TerrainType terrain){
        return tCache.get(terrain);
    }
    
    public String getTerrainAuthor(TerrainType terrain){
        return tAuthorCache.get(terrain);
    }
    
    public BufferedImage getConn(ConnType conn, int angle){
        if (! this.isImageAvailable(conn, angle) ) {
            System.err.format("Failure to load image for connector type %s%n"+
               "Connector (i.e. a graph edge) will be ignored.%n", conn.name());
            return null; 
        }
        return cache.get(conn).get(angle);
    }
    
    public boolean isImageAvailable(ConnType conn, int angle){
        Map<Integer, BufferedImage> temp = cache.get(conn);
        if (temp == null) return false;
        BufferedImage tempImage = temp.get(angle);
        if (tempImage == null) return false;
        return true;
    }
    
    private BufferedImage get(String fname){
        BufferedImage rval = null; 
        try {
            rval = ImageIO.read(new File(fname));
        } catch (Exception e) {
            System.err.println("ImageCache.get Warning: "+e.getMessage()+ " "+fname);
            System.err.println("..cd is "+(new File(".")).getAbsolutePath());
        }
        return rval;
    }

    public  FileAssn[] getBgAssn() {
    	return bgAssn;
    }
    
    public  FileAssn[] getTxpAssn() {
    	return txpAssn;
    }

    public  String getThemeConfigPath() {
    	return themeConfigPath;
    }
}