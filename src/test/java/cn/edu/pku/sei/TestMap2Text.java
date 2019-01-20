package cn.edu.pku.sei;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.mapStructures.TerrainType;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.util.NoiseFilterReader;

public class TestMap2Text  {

    @Test
    public void TestSaveOriginalMap() throws  IOException, ParseErrorException {
        System.out.println("\tTest for saving the original map!");
        System.out.println("BEGIN!");

        String oriFile = "src/maps/terrain_save_result.txt";
        String saveFile = "src/maps/terrain_save_result_new.txt";
        String content = "";

        FileReader oriFRdr = new FileReader(new File(oriFile));
        NoiseFilterReader oriRdr = new NoiseFilterReader(oriFRdr);

        TerrainLoader oriTr = new TerrainLoader(oriRdr);
        TerrainGraph oriGraph =  oriTr.load();

        System.out.println("========== Original Map Begin ==========");
        System.out.println(oriGraph.toString3());
        System.out.println("========== Original Map End ==========");

        content = oriGraph.toTxt();
        new MapFileWriter(content, new File(saveFile));

        FileReader saveFRdr = new FileReader(new File(saveFile));
        NoiseFilterReader saveRdr = new NoiseFilterReader(saveFRdr);

        TerrainLoader saveTr = new TerrainLoader(saveRdr);
        TerrainGraph saveGraph =  saveTr.load();

        System.out.println("========== Saved Map Begin ==========");
        System.out.println(saveGraph.toString3());
        System.out.println("========== Saved Map End ==========");

        assertEquals(saveGraph.toString3(), oriGraph.toString3());
    }

    @Test
    public void TestSaveCellAlteredMap() throws  IOException, ParseErrorException {
        System.out.println("\tTest for saving the cell-altered map!");
        System.out.println("BEGIN!");

        String oriFile = "src/maps/terrain_save_result.txt";
        String saveFile = "src/maps/terrain_save_result_new.txt";
        String content = "";

        FileReader oriFRdr = new FileReader(new File(oriFile));
        NoiseFilterReader oriRdr = new NoiseFilterReader(oriFRdr);

        TerrainLoader oriTr = new TerrainLoader(oriRdr);
        TerrainGraph oriGraph =  oriTr.load();

        System.out.println("========== Original Map Begin ==========");
        System.out.println(oriGraph.toString3());
        System.out.println("========== Original Map End ==========");

        oriGraph.setTerrain(0, 0, TerrainType.kUrban);
        oriGraph.setTerrain(0, 1, TerrainType.kWater);
        oriGraph.setTerrain(0, 2, TerrainType.kForest);
        oriGraph.setTerrain(0, 3, TerrainType.kAirport);

        content = oriGraph.toTxt();
        new MapFileWriter(content, new File(saveFile));

        FileReader saveFRdr = new FileReader(new File(saveFile));
        NoiseFilterReader saveRdr = new NoiseFilterReader(saveFRdr);

        TerrainLoader saveTr = new TerrainLoader(saveRdr);
        TerrainGraph saveGraph =  saveTr.load();

        System.out.println("========== Saved Map Begin ==========");
        System.out.println(saveGraph.toString3());
        System.out.println("========== Saved Map End ==========");

        assertEquals(saveGraph.toString3(), oriGraph.toString3());
    }

    @Test
    public void TestSaveNameAlteredMap() throws  IOException, ParseErrorException {
        System.out.println("\tTest for saving the name-altered map!");
        System.out.println("BEGIN!");

        String oriFile = "src/maps/terrain_save_result.txt";
        String saveFile = "src/maps/terrain_save_result_new.txt";
        String content = "";

        FileReader oriFRdr = new FileReader(new File(oriFile));
        NoiseFilterReader oriRdr = new NoiseFilterReader(oriFRdr);

        TerrainLoader oriTr = new TerrainLoader(oriRdr);
        TerrainGraph oriGraph =  oriTr.load();

        System.out.println("========== Original Map Begin ==========");
        System.out.println(oriGraph.toString3());
        System.out.println("========== Original Map End ==========");

        oriGraph.name = "someTestName";

        content = oriGraph.toTxt();
        new MapFileWriter(content, new File(saveFile));

        FileReader saveFRdr = new FileReader(new File(saveFile));
        NoiseFilterReader saveRdr = new NoiseFilterReader(saveFRdr);

        TerrainLoader saveTr = new TerrainLoader(saveRdr);
        TerrainGraph saveGraph =  saveTr.load();

        System.out.println("========== Saved Map Begin ==========");
        System.out.println(saveGraph.toString3());
        System.out.println("========== Saved Map End ==========");

        assertEquals(saveGraph.toString3(), oriGraph.toString3());
    }

    @Test
    public void TestSaveThemeAlteredMap() throws  IOException, ParseErrorException {
        System.out.println("\tTest for saving the theme-altered map!");
        System.out.println("BEGIN!");

        String oriFile = "src/maps/terrain_save_result.txt";
        String saveFile = "src/maps/terrain_save_result_new.txt";
        String content = "";

        FileReader oriFRdr = new FileReader(new File(oriFile));
        NoiseFilterReader oriRdr = new NoiseFilterReader(oriFRdr);

        TerrainLoader oriTr = new TerrainLoader(oriRdr);
        TerrainGraph oriGraph =  oriTr.load();

        System.out.println("========== Original Map Begin ==========");
        System.out.println(oriGraph.toString3());
        System.out.println("========== Original Map End ==========");

        oriGraph.setThemeName("light");

        content = oriGraph.toTxt();
        new MapFileWriter(content, new File(saveFile));

        FileReader saveFRdr = new FileReader(new File(saveFile));
        NoiseFilterReader saveRdr = new NoiseFilterReader(saveFRdr);

        TerrainLoader saveTr = new TerrainLoader(saveRdr);
        TerrainGraph saveGraph =  saveTr.load();

        System.out.println("========== Saved Map Begin ==========");
        System.out.println(saveGraph.toString3());
        System.out.println("========== Saved Map End ==========");

        assertEquals(saveGraph.toString3(), oriGraph.toString3());
    }
}
