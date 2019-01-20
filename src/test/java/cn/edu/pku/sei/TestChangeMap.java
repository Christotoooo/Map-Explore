package cn.edu.pku.sei;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.util.NoiseFilterReader;

@RunWith(Parameterized.class)
public class TestChangeMap {

    @Parameters
    public static Collection<Object[]> Data() {
        return Arrays.asList(new Object[][]{
                {"dark", "dark"},
                {"light", "light"},
                {"default", "default"}
        });
    }

    @Parameter
    public String tInput;
    @Parameter(1)
    public String tExpected;

    @Test
    public void TestThemeAlteredMap() throws  IOException, ParseErrorException {
        System.out.println("\tTest for altering!");
        System.out.println("BEGIN!");

        String file = "src/maps/terrain_save_result.txt";

        FileReader fRdr = new FileReader(new File(file));
        NoiseFilterReader rdr = new NoiseFilterReader(fRdr);

        TerrainLoader tr = new TerrainLoader(rdr);
        TerrainGraph graph =  tr.load();

        graph.setThemeName(tInput);

        System.out.println("========== Theme-Altered Map Begin ==========");
        System.out.println(graph.toString3());
        System.out.println("========== Theme-Altered Map End ==========");

        assertEquals(graph.getThemeName(), tExpected);
    }

    @Test
    public void TestNameAlteredMap() throws  IOException, ParseErrorException {
        System.out.println("\tTest for altering!");
        System.out.println("BEGIN!");

        String file = "src/maps/terrain_save_result.txt";

        FileReader fRdr = new FileReader(new File(file));
        NoiseFilterReader rdr = new NoiseFilterReader(fRdr);

        TerrainLoader tr = new TerrainLoader(rdr);
        TerrainGraph graph =  tr.load();

        graph.name = tInput;

        System.out.println("========== Theme-Altered Map Begin ==========");
        System.out.println(graph.toString3());
        System.out.println("========== Theme-Altered Map End ==========");

        assertEquals(graph.name, tExpected);
    }
}
