package cn.edu.pku.sei;

import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.mapStructures.TerrainType;
import cn.edu.pku.sei.shared.BareG;
import cn.edu.pku.sei.shared.BareV;
import cn.edu.pku.sei.shared.PathFinder;
import cn.edu.pku.sei.util.NoiseFilterReader;

@RunWith(Parameterized.class)
public class TestSaveImage {
	@Parameters
    public static Collection<Object[]> Data() {
        return Arrays.asList(new Object[][]{
                {"terrain_test_save.txt","src/maps/terrain_test_save.txt", "true"},
                {"minmap.txt","src/maps/terrain_test_save.txt", "false"}
        });
    }
    
    @Parameter
    public String tInputMap1;
    @Parameter(1)
    public String tInputMap2;
    @Parameter(2)
    public String expected;
    
	
	public static class Panel2Image{
		static byte[] getImagePixel(JPanel panel, String filename) {
			 BufferedImage bi = new BufferedImage ( panel.getWidth (), panel.getHeight (), BufferedImage.TYPE_INT_ARGB );
			 
			 Graphics2D g2d = bi.createGraphics ();
		        panel.paintAll ( g2d );
		        g2d.dispose ();

		        
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 try {
				ImageIO.write(bi, "png", baos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 byte[] pixels = baos.toByteArray();
			 return pixels;
		}
	}
	
	public TestSaveImage() {
		// TODO Auto-generated constructor stub
		 System.setProperty("java.awt.headless","true");
	}
	
	public static String md5Password(byte[] password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password);
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
	
	@Test
	public void testSave() {
		String[] args = {tInputMap1};
	
		try {
			Explore.main(args);
			byte[] img1 = Panel2Image.getImagePixel(Explore.app.nowBoard, "src/img1.png");
			// save
			File old_map = null;
			old_map	= new File(Explore.app.nowGraph.getSrcfile());
			old_map.renameTo(new File(Explore.app.nowGraph.getSrcfile() + "temp"));
			final File new_map = new File(Explore.app.nowGraph.getSrcfile());
			new MapFileWriter(Explore.app.nowGraph.toTxt(),new_map);
			old_map=new File(Explore.app.nowGraph.getSrcfile()+"temp");
			old_map.delete();
			// load
			try {
				FileReader new_fRdr = new FileReader(new File(tInputMap2));
				NoiseFilterReader new_rdr = null;
				new_rdr = new NoiseFilterReader(new_fRdr);
				TerrainLoader new_loader = null;
				new_loader = new TerrainLoader(new_rdr);
				Explore.app.nowBoard.setGraph(new_loader.load());
				Explore.app.nowGraph = Explore.app.nowBoard.getGraph();
				Explore.app.nowGraph.setSrcfile(new_map.getAbsolutePath());
				BareG g = Explore.app.nowGraph;
		        BareV start = g.getVertex(Explore.app.nowGraph.getStart().getLinear());
		        BareV goal  = g.getVertex(Explore.app.nowGraph.getGoal().getLinear());

		        List<Integer> result = PathFinder.findPath(g, start, goal);
		        Explore.app.nowGraph.setSolution(result);	   
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseErrorException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (HeadlessException e3) {
				System.out.println("Error: Need a Screen/Keyboard/Mouse...");
				e3.printStackTrace();
			}
			byte[] img2 = Panel2Image.getImagePixel(Explore.app.nowBoard, "src/img2.png");
			
			String img1md5 = md5Password(img1);
			String img2md5 = md5Password(img2);
			boolean result = img1md5.equals(img2md5);
			
			assertEquals(result?"true":"false", expected);
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (HeadlessException e3) {
			System.out.println("Error: Need a Screen/Keyboard/Mouse...");
			e3.printStackTrace();
		}
	}

	@Test
	public void testSaveChange() {
		String[] args = {tInputMap1};
		
		try {
			Explore.main(args);
			byte[] img1 = Panel2Image.getImagePixel(Explore.app.nowBoard, "src/img1.png");
			// save
			boolean exp_result = Explore.app.nowGraph.getVertex(0, 0).getData().getTerrain() == TerrainType.kBrush?true:false;
			Explore.app.nowGraph.setTerrain(0, 0, TerrainType.kBrush);
			File old_map = null;
			old_map	= new File(Explore.app.nowGraph.getSrcfile());
			old_map.renameTo(new File(Explore.app.nowGraph.getSrcfile() + "temp"));
			final File new_map = new File(Explore.app.nowGraph.getSrcfile());
			new MapFileWriter(Explore.app.nowGraph.toTxt(),new_map);
			old_map=new File(Explore.app.nowGraph.getSrcfile()+"temp");
			old_map.delete();
			// load
			try {
				FileReader new_fRdr = new FileReader(new File("src/maps/"+tInputMap1));
				NoiseFilterReader new_rdr = null;
				new_rdr = new NoiseFilterReader(new_fRdr);
				TerrainLoader new_loader = null;
				new_loader = new TerrainLoader(new_rdr);
				Explore.app.nowBoard.setGraph(new_loader.load());
				Explore.app.nowGraph = Explore.app.nowBoard.getGraph();
				Explore.app.nowGraph.setSrcfile(new_map.getAbsolutePath());
				BareG g = Explore.app.nowGraph;
		        BareV start = g.getVertex(Explore.app.nowGraph.getStart().getLinear());
		        BareV goal  = g.getVertex(Explore.app.nowGraph.getGoal().getLinear());

		        List<Integer> result = PathFinder.findPath(g, start, goal);
		        Explore.app.nowGraph.setSolution(result);	   
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseErrorException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (HeadlessException e3) {
				System.out.println("Error: Need a Screen/Keyboard/Mouse...");
				e3.printStackTrace();
			}
			byte[] img2 = Panel2Image.getImagePixel(Explore.app.nowBoard, "src/img2.png");
			
			String img1md5 = md5Password(img1);
			String img2md5 = md5Password(img2);
			boolean result = img1md5.equals(img2md5);
			
			assertEquals(result, exp_result);
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (HeadlessException e3) {
			System.out.println("Error: Need a Screen/Keyboard/Mouse...");
			e3.printStackTrace();
		}
	}
}
