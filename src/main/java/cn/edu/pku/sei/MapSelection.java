//group 3 story 9
package cn.edu.pku.sei;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MapSelection extends JFrame {
    private static final int DEFAULT_WIDTH=400;
    private static final int DEFAULT_HEIGHT=300;
    private JPanel listPanel;
    private String[] maps;
    private JList<String> wordlist;
    private JPanel buttonPanel;
    private String mapDir = "src/maps";
    private String[] mapFiles = {"terrain.txt"};
    public static int openedGraph = 0;

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(String map:maps){
            s.append(map);
        }
        return  s.toString();

    }

    public MapSelection(){
        this.setName("Maps");
        List<String> mapList = getAllFile(mapDir);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        maps = mapList.toArray(new String[mapList.size()]);
        wordlist = new JList<>(maps);
        wordlist.setVisibleRowCount(7);
        // forbid JList's multiple selection
        wordlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        JScrollPane scrollPane = new JScrollPane(wordlist);

        listPanel = new JPanel();
        listPanel.add(scrollPane);
        wordlist.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
            	// list supports multiple selecting function,
            	// but in the version ce2074, multiple selection leads to bug
                ArrayList<String>  tFiles = new ArrayList<>();
                for(String value : wordlist.getSelectedValuesList()){
                    tFiles.add(value);
                }
                mapFiles = tFiles.toArray(new String[tFiles.size()]);
            }

        });


        buttonPanel = new JPanel();
        JButton button = new JButton("Render");
        button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                /*if(openedGraph != 0) {
                	JOptionPane.showMessageDialog(null, "You have opened one window.\nPlease close it first.");  
                	return;
                }*/
                //for(String mapFile : mapFiles){
                try {
                    Explore.main(new String[]{mapFiles[0]});
                }
                catch (Exception ex){

                }
                openedGraph += 1;
                //}
            }

        });
        buttonPanel.add(button);

        this.add(listPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setTitle("Select a map");
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        new MapSelection();
    }
    private static List<String> getAllFile(String directoryPath) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            System.out.println("directory not exist");
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //list.addAll(getAllFile(file.getAbsolutePath()));
            } else {
                list.add(file.getName());
            }
        }
        return list;
    }
}

