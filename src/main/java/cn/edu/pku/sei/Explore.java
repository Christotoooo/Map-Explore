package cn.edu.pku.sei;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainBoard;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.shared.BareG;
import cn.edu.pku.sei.shared.BareV;
import cn.edu.pku.sei.shared.PathFinder;
import cn.edu.pku.sei.util.DirectoryProbe;
import cn.edu.pku.sei.util.NoiseFilterReader;

public class Explore {

    private static Logger logger = new NullLogger();
    private String mapsloc;
    private String file;
    static Explore app;

    public static void main(String[] args) throws InterruptedException, IOException{

        String file = "terrain_save_result.txt";
        String mapsloc = DirectoryProbe.probe("maps", "src/maps");
        if (mapsloc == null)
        {
            throw new FileNotFoundException("Cannot find maps directory.");
        }
        System.out.println("Using maps from "+(new File(".")).getCanonicalPath()+"/"+mapsloc);
        if (args.length < 1)
        {
            System.out.println("Explore takes a filename as argument\n"
                    + "Using default file "+file);
        } else {
           file = args[0];
           if (file.startsWith(File.pathSeparator))
           {
               mapsloc = "";
           }
        }


        app = new Explore();//group 3
        try {
        
            FileReader fRdr = new FileReader(new File(mapsloc,file));
            NoiseFilterReader rdr = new NoiseFilterReader(fRdr);
            app.mapsloc = mapsloc;
            app.file = file;
            app.run(rdr);
            Thread.currentThread().sleep(500);	//changed from 5000 to 500
        } catch (ParseErrorException e){
            e.printStackTrace();//group 3
            MapSelection.openedGraph -= 1;//group 3
        } catch (IOException e){
           e.printStackTrace();
        }
    }
    
    
    public void run(NoiseFilterReader rdr ) throws ParseErrorException {
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        BareG g = tGraph;
        BareV start = g.getVertex(tGraph.getStart().getLinear());
        BareV goal  = g.getVertex(tGraph.getGoal().getLinear());

        List<Integer> result = PathFinder.findPath(g, start, goal);
        tGraph.setSolution(result);
        //System.out.println("=============result===============\n");
        //System.out.println(result);
        //System.out.println("=============result===============\n");
        //don't comment out this line!!! by group 3
        logger.log(result.toString());
        
        display(tGraph);
        System.out.println("=============TXT===============\n");
        System.out.print(tGraph.toTxt());
        nowGraph.setSrcfile(new File(mapsloc,file).getAbsolutePath());
    }
    
    JFrame nowFrame;
    TerrainBoard nowBoard;
    TerrainGraph nowGraph;
    JFrame property=new JFrame();
    JTextField new_title=new JTextField(10);
    JComboBox<String> themeBox;
    public static StatusDisplay status = new StatusDisplay();  //group5 status display
    
    public void display(TerrainGraph g)
    {
    	this.nowGraph=g;
        TerrainBoard m = new TerrainBoard(g);
        this.nowBoard=m;
        int w = g.width;
        int h = g.height;
        m.setPreferredSize(new Dimension(w*144,h*144));
        m.addMouseListener(new GraphMouseListener(m)); //group 3 story 57
        JFrame f = new JFrame();
        this.nowFrame=f;
        f.setName("frame"+(Coordinate.getCnt()-1));//group 3
        f.addWindowFocusListener(new FocusListener());//group 3

        
        f.addWindowListener(new GraphWindowListener());//group 3 story 9
        f.setSize(1000, 1000);//group 3 story 6
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//group 3 story 9
        
        JScrollPane jsp = new JScrollPane( m );   //group 5 status display
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(0,0));
        content.add(jsp,BorderLayout.CENTER);
        content.add(status.getLabel(),BorderLayout.SOUTH);
        f.setContentPane(content);
        
        status.displayStatus("test first");         //group 5 status display test
        status.displayStatus("test second");
        
        f.setVisible(true);
        
//        Scanner scan = new Scanner(System.in);
        JMenuBar bar=new JMenuBar();

        // group1 file menu
        JMenu file = new JMenu("file");        
        bar.add(file);
        JMenuItem save=new JMenuItem("save");
        save.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
				File old_map = null;
				old_map	= new File(nowGraph.getSrcfile());
				old_map.renameTo(new File(nowGraph.getSrcfile() + "temp"));
				final File new_map = new File(nowGraph.getSrcfile());
				new MapFileWriter(nowGraph.toTxt(),new_map);
				old_map=new File(nowGraph.getSrcfile()+"temp");
				old_map.delete();
			}
        });
        file.add(save);
        JMenuItem saveAs=new JMenuItem("save as");
        saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
            {
				JFileChooser chooser = new JFileChooser("src/maps");
				chooser.showSaveDialog(nowFrame);
				File save_map = chooser.getSelectedFile();
				System.out.println(save_map.getAbsolutePath());
				if(save_map.exists()) 
				{
					JOptionPane.showMessageDialog(nowFrame, "file already exists", "error", JOptionPane.ERROR_MESSAGE);
				}
				else 
				{
					new MapFileWriter(nowGraph.toTxt(),save_map);
				}
			}
        });
        file.add(saveAs);
        JMenuItem load=new JMenuItem("load");
        load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
            {
				JFileChooser chooser = new JFileChooser("src/maps");
				chooser.showOpenDialog(nowFrame);
				final File new_map = chooser.getSelectedFile();
				try {
					FileReader new_fRdr = new FileReader(new_map);
					NoiseFilterReader new_rdr = null;
					new_rdr = new NoiseFilterReader(new_fRdr);
					TerrainLoader new_loader = null;
					new_loader = new TerrainLoader(new_rdr);
					nowBoard.setGraph(new_loader.load());
					nowGraph = nowBoard.getGraph();
					nowGraph.setSrcfile(new_map.getAbsolutePath());
					BareG g = nowGraph;
			        BareV start = g.getVertex(nowGraph.getStart().getLinear());
			        BareV goal  = g.getVertex(nowGraph.getGoal().getLinear());

			        List<Integer> result = PathFinder.findPath(g, start, goal);
			        nowGraph.setSolution(result);
					nowFrame.revalidate();
					nowFrame.repaint();
					
				} catch (FileNotFoundException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				catch (ParseErrorException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
        });
        file.add(load);
        // end group1 file menu
        JMenu option=new JMenu("edit");      
    	
    	//group1 help&about menu
        bar.add(option);
        
        JMenuItem properties=new JMenuItem("properties");    
        property.setTitle("properties");
        property.setMinimumSize(new Dimension(500,300));
        property.setPreferredSize(new Dimension(500,300));
        JTabbedPane jp=new JTabbedPane(JTabbedPane.LEFT);
        jp.setFont(new Font("微软雅黑",Font.PLAIN,18));
        JPanel p1=new JPanel(new BorderLayout());  
        
        JLabel lab1=new JLabel("please enter new title here, (do not contain space)");
        p1.add(lab1,BorderLayout.NORTH);
        
        p1.add(new_title,BorderLayout.CENTER);
        
        JButton apply1=new JButton("Apply");
        apply1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) 
			{

				String str=new_title.getText();
				if(str.contains(" ")) {
					JOptionPane.showMessageDialog(property, "please do not contain space in title", "error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					nowGraph.name=str;
					nowFrame.revalidate();
					nowFrame.repaint();
				}
			}
        });
        p1.add(apply1,BorderLayout.SOUTH);
        
        JPanel p2=new JPanel(new BorderLayout());
        JLabel lab2=new JLabel("select a theme");
        p2.add(lab2,BorderLayout.NORTH);
        
        // group1 change theme
        String conf_path = "src/config/theme.conf";
    	File conf_file = null;
    	conf_file = new File(conf_path);
    	String theme_name = "";
        Vector<String> themeList=new Vector<String>();
        InputStreamReader reader;
		try {
			reader = new InputStreamReader(
					new FileInputStream(conf_file));
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			while(line != null) 
			{
//				System.out.println(line);
				if (line.length() <= 0 || line.charAt(0) == '#')
				{
					line = br.readLine();
					continue;
				}
				if (line.contains(".conf")) 
				{
					theme_name = line.split(" |\t")[0];
					themeList.add(theme_name);
					
					line = br.readLine();
					continue;
				}
				line=br.readLine();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		themeBox=new JComboBox<String>(themeList);
        p2.add(themeBox, BorderLayout.CENTER);
        
        JButton apply2=new JButton("Apply");
        apply2.addMouseListener(new MouseAdapter() {
            @Override
			public void mousePressed(MouseEvent arg0)
			{
				String str=(String) themeBox.getSelectedItem();
                nowGraph.setThemeName(str);
                nowBoard.changeInstance();
                nowFrame.revalidate();
                nowFrame.repaint();
			}
        });
        p2.add(apply2,BorderLayout.SOUTH);
        
        jp.add("title", p1);
        jp.add("theme", p2);
        
        
        
        property.setContentPane(jp);
        property.setVisible(false);
        
        properties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				property.setVisible(true);
			}
        });
        option.add(properties);
        
        JMenu help=new JMenu("help");
        JMenuItem about=new JMenuItem("about explore");
        about.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
				PopHelppage.popPage("src/helper/index.html");
			}
        });
        help.add(about);
        
        JMenuItem feature=new JMenuItem("terrain features");
        feature.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("adtion performed");
				PopHelppage.popPage("src/helper/index2.html");
			}
        });
        help.add(feature);
        
        JMenuItem theme1=new JMenuItem("theme");
        theme1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				PopHelppage.popPage("src/helper/index5.html");
			}
        });
        help.add(theme1);
        
        JMenuItem initial=new JMenuItem("initial");
        initial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				PopHelppage.popPage("src/helper/index6.html");
			}
        });
        help.add(initial);
        
        
        bar.add(help);
        
        f.setJMenuBar(bar);
        bar.setVisible(true);
        f.revalidate();
        f.repaint();

        //scan.nextLine();
    }


	

	public static void setLogger(Logger logger) {
		Explore.logger = logger;
	}


	public static Object getLogger() {
		
		return logger;
	}

}
