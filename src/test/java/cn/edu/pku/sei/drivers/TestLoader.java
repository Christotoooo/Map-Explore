package cn.edu.pku.sei.drivers;

import java.awt.Dimension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.junit.Ignore;
import org.junit.Test;

import cn.edu.pku.sei.TerrainLoader;
import cn.edu.pku.sei.mapStructures.ParseErrorException;

import cn.edu.pku.sei.mapStructures.TerrainType;

import cn.edu.pku.sei.rendering.TerrainBoard;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.shared.BareG;
import cn.edu.pku.sei.shared.BareV;
import cn.edu.pku.sei.shared.PathFinder;
import cn.edu.pku.sei.util.DirectoryProbe;
import cn.edu.pku.sei.util.NoiseFilterReader;


import static org.junit.Assert.*;

public class TestLoader {

    String eol = System.lineSeparator();
    String minMap = 
            "geometry   5 6 "+eol+eol+
            "flags 2 3 5 5"+eol+eol
            ;
    
    String terrain = 
            "water "+eol
            + "1 3 2 4"+eol+eol+
            "brush "+eol
            + "0 4 0 5 1 5 1 6 2 6 3 7"+eol+eol+
            "forest "+eol
            + "3 3 3 4 3 5 4 4 4 5 4 6"+eol+eol
            ;
    
  //Group 5 added
    final String terrain_group_5 =
    		"geometry 7 7"+eol+eol+
    		"flags 2 3 3 5"+eol+eol+
    		"water 1 3 2 4"+eol+eol+
    		"brush 0 4 0 5 1 5 1 6 2 6 3 7"+eol+eol+
    		"forest 3 3 3 4 3 5 4 4 4 5 4 6"+eol+eol+
    		"hwy 2 3 3 3 4 3"+eol+eol+
    		"unpaved 3 5 4 5 4 4"+eol+eol+
    		"dividedhwy 0 3 1 4 1 5 2 6 3 7"+eol+eol+
    		"bridge 1 1 2 2 3 2"+eol+eol+
    		"river 2 1 2 2 1 2 1 3"+eol+eol+
    		"river 0 1 1 2 1 3"+eol+eol+
    		"barrierwall" + eol+eol+
    		"  2 3 ul dl dn" +
    		"  3 4 dl dn"+eol+eol+
    		"mountain" +eol+eol+ 
    		" 3 5 dl dn" +
    		" 4 6 dl dn"+eol+eol+
    		"notes"+eol+
    		"This is the demo map. We're gonna use this map as an example of adding notes." + eol+
    		"For the convenience of developers, we can add notes at the end of each file " + eol+
    		"so that developers can easily know what's in the map without running the code." + eol+
    		"When the reader read the keyword 'notes', it will ignore all the contents below."+eol+
    		"So notes can be used only at the end of each file."+eol+
    		eol+
    		"In this map, there's a forest area in the middle of the map with a dirt road in "+eol+
    		"it. At the south boundary of the forest lies a mountain. There's a lake occuping"+eol+
    		"two cells in the west of the forest, with a barrierwall setting at the boundary."+eol+
    		"Two rivers in the north-west corner merges in the lake. A bridge is set across "+eol+
    		"one of the river.The south-west corner of the map is occupied with a brush area."+eol+
    		"A four-lane highway passes through the brush to the flat land.";
    
    String paths = 
            "hwy 2 3 3 3 4 3"+eol+eol+
            "unpaved 3 5 4 5 4 4"+eol+eol+
            "dividedhwy 0 3 1 4 1 5 2 6 3 7"+eol+eol+
            "river 2 1 2 2 1 2 1 3"+eol+eol;
    
    String borders = 
            "barrierwall 2 3 ul dl dn "
            + "3 4 dl dn" +eol+eol
            +"mountain 3 5 dl dn "
            + "4 6 dl dn"+eol+eol;
    
    String solution= 
            "testpath 2 3 2 2 1 2 0 2 0 3"+eol+eol;
    
    
    String expectedGroup5=
    		"0: (0, 1: 165) (0, 7: 165) \n" + 
    		"1: (1, 0: 165) (1, 2: 165) (1, 7: 165) (1, 8: 165) (1, 9: 130) \n" + 
    		"2: (2, 1: 165) (2, 3: 165) (2, 9: 165) \n" + 
    		"3: (3, 10: 195) (3, 11: 165) (3, 2: 165) (3, 4: 165) (3, 9: 130) \n" + 
    		"4: (4, 11: 165) (4, 3: 165) (4, 5: 165) \n" + 
    		"5: (5, 11: 165) (5, 12: 165) (5, 13: 165) (5, 4: 165) (5, 6: 165) \n" + 
    		"6: (6, 13: 165) (6, 5: 165) \n" + 
    		"7: (7, 0: 165) (7, 14: 165) (7, 1: 165) (7, 8: 165) \n" + 
    		"8: (8, 14: 165) (8, 15: 185) (8, 16: 900) (8, 1: 165) (8, 7: 165) (8, 9: 165) \n" + 
    		"9: (9, 10: 195) (9, 16: 165) (9, 1: 130) (9, 2: 165) (9, 3: 130) (9, 8: 165) \n" + 
    		"10: (10, 11: 130) (10, 16: 130) (10, 17: 195) (10, 18: 195) (10, 3: 195) (10, 9: 195) \n" + 
    		"11: (11, 10: 130) (11, 12: 165) (11, 18: 195) (11, 3: 165) (11, 4: 165) (11, 5: 165) \n" + 
    		"12: (12, 11: 165) (12, 13: 165) (12, 18: 195) (12, 19: 165) (12, 20: 165) (12, 5: 165) \n" + 
    		"13: (13, 12: 165) (13, 20: 165) (13, 5: 165) (13, 6: 165) \n" + 
    		"14: (14, 15: 190) (14, 21: 165) (14, 7: 165) (14, 8: 165) \n" + 
    		"15: (15, 14: 190) (15, 16: 900) (15, 21: 190) (15, 22: 190) (15, 23: 190) (15, 8: 185) \n" + 
    		"16: (16, 10: 130) (16, 15: 900) (16, 17: 195) (16, 23: 900) (16, 8: 900) (16, 9: 165) \n" + 
    		"17: (17, 10: 195) (17, 16: 195) (17, 18: 195) (17, 23: 900) (17, 24: 900) (17, 25: 195) \n" + 
    		"18: (18, 10: 195) (18, 11: 195) (18, 12: 195) (18, 17: 195) (18, 19: 195) (18, 25: 143) \n" + 
    		"19: (19, 12: 165) (19, 18: 195) (19, 20: 165) (19, 25: 195) (19, 26: 165) (19, 27: 165) \n" + 
    		"20: (20, 12: 165) (20, 13: 165) (20, 19: 165) (20, 27: 165) \n" + 
    		"21: (21, 14: 165) (21, 15: 190) (21, 22: 110) (21, 28: 180) \n" + 
    		"22: (22, 15: 190) (22, 21: 110) (22, 23: 190) (22, 28: 180) (22, 29: 110) (22, 30: 165) \n" + 
    		"23: (23, 15: 190) (23, 16: 900) (23, 17: 900) (23, 22: 190) (23, 24: 195) (23, 30: 190) \n" + 
    		"24: (24, 17: 900) (24, 23: 195) (24, 25: 143) (24, 30: 1200) (24, 31: 1200) (24, 32: 195) \n" + 
    		"25: (25, 17: 195) (25, 18: 143) (25, 19: 195) (25, 24: 143) (25, 26: 195) (25, 32: 195) \n" + 
    		"26: (26, 19: 165) (26, 25: 195) (26, 27: 165) (26, 32: 195) (26, 33: 165) (26, 34: 165) \n" + 
    		"27: (27, 19: 165) (27, 20: 165) (27, 26: 165) (27, 34: 165) \n" + 
    		"28: (28, 21: 180) (28, 22: 180) (28, 29: 180) (28, 35: 180) \n" + 
    		"29: (29, 22: 110) (29, 28: 180) (29, 30: 180) (29, 35: 180) (29, 36: 180) (29, 37: 110) \n" + 
    		"30: (30, 22: 165) (30, 23: 190) (30, 24: 1200) (30, 29: 180) (30, 31: 165) (30, 37: 180) \n" + 
    		"31: (31, 24: 1200) (31, 30: 165) (31, 32: 1200) (31, 37: 180) (31, 38: 180) (31, 39: 165) \n" + 
    		"32: (32, 24: 195) (32, 25: 195) (32, 26: 195) (32, 31: 1200) (32, 33: 195) (32, 39: 1200) \n" + 
    		"33: (33, 26: 165) (33, 32: 195) (33, 34: 165) (33, 39: 165) (33, 40: 165) (33, 41: 165) \n" + 
    		"34: (34, 26: 165) (34, 27: 165) (34, 33: 165) (34, 41: 165) \n" + 
    		"35: (35, 28: 180) (35, 29: 180) (35, 36: 180) (35, 42: 180) \n" + 
    		"36: (36, 29: 180) (36, 35: 180) (36, 37: 180) (36, 42: 180) (36, 43: 180) (36, 44: 180) \n" + 
    		"37: (37, 29: 110) (37, 30: 180) (37, 31: 180) (37, 36: 180) (37, 38: 110) (37, 44: 180) \n" + 
    		"38: (38, 31: 180) (38, 37: 110) (38, 39: 180) (38, 44: 180) (38, 45: 180) (38, 46: 180) \n" + 
    		"39: (39, 31: 165) (39, 32: 1200) (39, 33: 165) (39, 38: 180) (39, 40: 165) (39, 46: 165) \n" + 
    		"40: (40, 33: 165) (40, 39: 165) (40, 41: 165) (40, 46: 165) (40, 47: 165) (40, 48: 165) \n" + 
    		"41: (41, 33: 165) (41, 34: 165) (41, 40: 165) (41, 48: 165) \n" + 
    		"42: (42, 35: 180) (42, 36: 180) (42, 43: 165) \n" + 
    		"43: (43, 36: 180) (43, 42: 165) (43, 44: 165) \n" + 
    		"44: (44, 36: 180) (44, 37: 180) (44, 38: 180) (44, 43: 165) (44, 45: 165) \n" + 
    		"45: (45, 38: 180) (45, 44: 165) (45, 46: 165) \n" + 
    		"46: (46, 38: 180) (46, 39: 165) (46, 40: 165) (46, 45: 165) (46, 47: 165) \n" + 
    		"47: (47, 40: 165) (47, 46: 165) (47, 48: 165) \n" + 
    		"48: (48, 40: 165) (48, 41: 165) (48, 47: 165) \n" + 
    		"noname" ;
    String copiedFromConsole2 =
		 "0: (0, 1: 165) (0, 7: 165) \n" + 
		 "1: (1, 0: 165) (1, 2: 165) (1, 7: 165) (1, 8: 165) (1, 9: 130) \n" + 
		 "2: (2, 1: 165) (2, 3: 165) (2, 9: 165) \n" + 
		 "3: (3, 10: 195) (3, 11: 165) (3, 2: 165) (3, 4: 165) (3, 9: 130) \n" + 
		 "4: (4, 11: 165) (4, 3: 165) (4, 5: 165) \n" + 
		 "5: (5, 11: 165) (5, 12: 165) (5, 13: 165) (5, 4: 165) (5, 6: 165) \n" + 
		 "6: (6, 13: 165) (6, 5: 165) \n" + 
		 "7: (7, 0: 165) (7, 14: 165) (7, 1: 165) (7, 8: 165) \n" + 
		 "8: (8, 14: 165) (8, 15: 185) (8, 16: 900) (8, 1: 165) (8, 7: 165) (8, 9: 165) \n" + 
		 "9: (9, 10: 195) (9, 16: 165) (9, 1: 130) (9, 2: 165) (9, 3: 130) (9, 8: 165) \n" + 
		 "10: (10, 11: 130) (10, 16: 130) (10, 17: 195) (10, 18: 195) (10, 3: 195) (10, 9: 195) \n" + 
		 "11: (11, 10: 130) (11, 12: 165) (11, 18: 195) (11, 3: 165) (11, 4: 165) (11, 5: 165) \n" + 
		 "12: (12, 11: 165) (12, 13: 165) (12, 18: 195) (12, 19: 165) (12, 20: 165) (12, 5: 165) \n" + 
		 "13: (13, 12: 165) (13, 20: 165) (13, 5: 165) (13, 6: 165) \n" + 
		 "14: (14, 15: 190) (14, 21: 165) (14, 7: 165) (14, 8: 165) \n" + 
		 "15: (15, 14: 190) (15, 16: 900) (15, 21: 190) (15, 22: 190) (15, 23: 190) (15, 8: 185) \n" + 
		 "16: (16, 10: 130) (16, 15: 900) (16, 17: 195) (16, 23: 900) (16, 8: 900) (16, 9: 165) \n" + 
		 "17: (17, 10: 195) (17, 16: 195) (17, 18: 195) (17, 23: 900) (17, 24: 900) (17, 25: 195) \n" + 
		 "18: (18, 10: 195) (18, 11: 195) (18, 12: 195) (18, 17: 195) (18, 19: 195) (18, 25: 143) \n" + 
		 "19: (19, 12: 165) (19, 18: 195) (19, 20: 165) (19, 25: 195) (19, 26: 165) (19, 27: 165) \n" + 
		 "20: (20, 12: 165) (20, 13: 165) (20, 19: 165) (20, 27: 165) \n" + 
		 "21: (21, 14: 165) (21, 15: 190) (21, 22: 110) (21, 28: 180) \n" + 
		 "22: (22, 15: 190) (22, 21: 110) (22, 23: 190) (22, 28: 180) (22, 29: 110) (22, 30: 165) \n" + 
		 "23: (23, 15: 190) (23, 16: 900) (23, 17: 900) (23, 22: 190) (23, 24: 195) (23, 30: 190) \n" + 
		 "24: (24, 17: 900) (24, 23: 195) (24, 25: 143) (24, 30: 195) (24, 31: 195) (24, 32: 195) \n" + 
		 "25: (25, 17: 195) (25, 18: 143) (25, 19: 195) (25, 24: 143) (25, 26: 195) (25, 32: 195) \n" + 
		 "26: (26, 19: 165) (26, 25: 195) (26, 27: 165) (26, 32: 195) (26, 33: 165) (26, 34: 165) \n" + 
		 "27: (27, 19: 165) (27, 20: 165) (27, 26: 165) (27, 34: 165) \n" + 
		 "28: (28, 21: 180) (28, 22: 180) (28, 29: 180) (28, 35: 180) \n" + 
		 "29: (29, 22: 110) (29, 28: 180) (29, 30: 180) (29, 35: 180) (29, 36: 180) (29, 37: 110) \n" + 
		 "30: (30, 22: 165) (30, 23: 190) (30, 24: 195) (30, 29: 180) (30, 31: 165) (30, 37: 180) \n" + 
		 "31: (31, 24: 195) (31, 30: 165) (31, 32: 195) (31, 37: 180) (31, 38: 180) (31, 39: 165) \n" + 
		 "32: (32, 24: 195) (32, 25: 195) (32, 26: 195) (32, 31: 195) (32, 33: 195) (32, 39: 195) \n" + 
		 "33: (33, 26: 165) (33, 32: 195) (33, 34: 165) (33, 39: 165) (33, 40: 165) (33, 41: 165) \n" + 
		 "34: (34, 26: 165) (34, 27: 165) (34, 33: 165) (34, 41: 165) \n" + 
		 "35: (35, 28: 180) (35, 29: 180) (35, 36: 180) (35, 42: 180) \n" + 
		 "36: (36, 29: 180) (36, 35: 180) (36, 37: 180) (36, 42: 180) (36, 43: 180) (36, 44: 180) \n" + 
		 "37: (37, 29: 110) (37, 30: 180) (37, 31: 180) (37, 36: 180) (37, 38: 110) (37, 44: 180) \n" + 
		 "38: (38, 31: 180) (38, 37: 110) (38, 39: 180) (38, 44: 180) (38, 45: 180) (38, 46: 180) \n" + 
		 "39: (39, 31: 165) (39, 32: 195) (39, 33: 165) (39, 38: 180) (39, 40: 165) (39, 46: 165) \n" + 
		 "40: (40, 33: 165) (40, 39: 165) (40, 41: 165) (40, 46: 165) (40, 47: 165) (40, 48: 165) \n" + 
		 "41: (41, 33: 165) (41, 34: 165) (41, 40: 165) (41, 48: 165) \n" + 
		 "42: (42, 35: 180) (42, 36: 180) (42, 43: 165) \n" + 
		 "43: (43, 36: 180) (43, 42: 165) (43, 44: 165) \n" + 
		 "44: (44, 36: 180) (44, 37: 180) (44, 38: 180) (44, 43: 165) (44, 45: 165) \n" + 
		 "45: (45, 38: 180) (45, 44: 165) (45, 46: 165) \n" + 
		 "46: (46, 38: 180) (46, 39: 165) (46, 40: 165) (46, 45: 165) (46, 47: 165) \n" + 
		 "47: (47, 40: 165) (47, 46: 165) (47, 48: 165) \n" + 
		 "48: (48, 40: 165) (48, 41: 165) (48, 47: 165) \n" + 
		 "noname";
    
    String resForTestPaths = "0: (0, 1: 165) (0, 5: 165) \n" + 
            "1: (1, 0: 165) (1, 2: 165) (1, 5: 165) (1, 6: 165) (1, 7: 165) \n" + 
            "2: (2, 1: 165) (2, 3: 165) (2, 7: 165) \n" + 
            "3: (3, 2: 165) (3, 4: 165) (3, 7: 165) (3, 8: 195) (3, 9: 165) \n" + 
            "4: (4, 3: 165) (4, 9: 165) \n" + 
            "5: (5, 0: 165) (5, 10: 165) (5, 1: 165) (5, 6: 165) \n" + 
            "6: (6, 10: 165) (6, 11: 185) (6, 12: 165) (6, 1: 165) (6, 5: 165) (6, 7: 165) \n" + 
            "7: (7, 12: 165) (7, 1: 165) (7, 2: 165) (7, 3: 165) (7, 6: 165) (7, 8: 195) \n" + 
            "8: (8, 12: 130) (8, 13: 195) (8, 14: 195) (8, 3: 195) (8, 7: 195) (8, 9: 130) \n" + 
            "9: (9, 14: 195) (9, 3: 165) (9, 4: 165) (9, 8: 130) \n" + 
            "10: (10, 11: 190) (10, 15: 165) (10, 5: 165) (10, 6: 165) \n" + 
            "11: (11, 10: 190) (11, 12: 190) (11, 15: 190) (11, 16: 190) (11, 17: 190) (11, 6: 185) \n" + 
            "12: (12, 11: 190) (12, 13: 195) (12, 17: 190) (12, 6: 165) (12, 7: 165) (12, 8: 130) \n" + 
            "13: (13, 12: 195) (13, 14: 195) (13, 17: 195) (13, 18: 195) (13, 19: 195) (13, 8: 195) \n" + 
            "14: (14, 13: 195) (14, 19: 143) (14, 8: 195) (14, 9: 195) \n" + 
            "15: (15, 10: 165) (15, 11: 190) (15, 16: 110) (15, 20: 180) \n" + 
            "16: (16, 11: 190) (16, 15: 110) (16, 17: 190) (16, 20: 180) (16, 21: 110) (16, 22: 165) \n" + 
            "17: (17, 11: 190) (17, 12: 190) (17, 13: 195) (17, 16: 190) (17, 18: 195) (17, 22: 190) \n" + 
            "18: (18, 13: 195) (18, 17: 195) (18, 19: 143) (18, 22: 195) (18, 23: 195) (18, 24: 195) \n" + 
            "19: (19, 13: 195) (19, 14: 143) (19, 18: 143) (19, 24: 195) \n" + 
            "20: (20, 15: 180) (20, 16: 180) (20, 21: 180) (20, 25: 180) \n" + 
            "21: (21, 16: 110) (21, 20: 180) (21, 22: 180) (21, 25: 180) (21, 26: 180) (21, 27: 110) \n" + 
            "22: (22, 16: 165) (22, 17: 190) (22, 18: 195) (22, 21: 180) (22, 23: 165) (22, 27: 180) \n" + 
            "23: (23, 18: 195) (23, 22: 165) (23, 24: 195) (23, 27: 180) (23, 28: 180) (23, 29: 165) \n" + 
            "24: (24, 18: 195) (24, 19: 195) (24, 23: 195) (24, 29: 195) \n" + 
            "25: (25, 20: 180) (25, 21: 180) (25, 26: 180) \n" + 
            "26: (26, 21: 180) (26, 25: 180) (26, 27: 180) \n" + 
            "27: (27, 21: 110) (27, 22: 180) (27, 23: 180) (27, 26: 180) (27, 28: 110) \n" + 
            "28: (28, 23: 180) (28, 27: 110) (28, 29: 180) \n" + 
            "29: (29, 23: 165) (29, 24: 195) (29, 28: 180) \n" +
            "noname";
    
    @Test
  //Group 5 added: Test all four of our stories
    
    public void testGroup5() throws ParseErrorException{ 
        Reader rdr = new NoiseFilterReader(new StringReader(terrain_group_5));
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();    
        System.out.println("----------\n"+tGraph.toString()+"\n----------\n"+expectedGroup5);
        //display(tGraph);
 
        assertEquals(expectedGroup5, tGraph.toString());

    } 
    
    //group 5 added, test story #64
    @Test
    public void testToStringGroup5() throws ParseErrorException{
    	String initialResult,result;
    	int flag=0;
    	Reader rdr = new NoiseFilterReader(new StringReader(terrain_group_5));
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        initialResult=tGraph.toString();
        for (int i=0;i<100;i++) {
        	result=tGraph.toString();
//        	if(i==50) {
//        		result=result+"000";
//        	}
        	if(result.equals(initialResult)) {
        		flag+=1;
        	}
        	
        }
        assertEquals(flag,100);
    }
    
    /**
     * Description: fix the non-tests to be capable of failure.
     * @author Group 3 Story #68
     * @throws ParseErrorException
     * @throws IOException
     */
    
    @Test
    public void testFromFile() throws ParseErrorException, IOException {
        String file = "terrain_group_5.txt";
        String mapsloc = DirectoryProbe.probe("maps", "src/maps");
        if (mapsloc == null){
            throw new FileNotFoundException("Cannot find maps directory.");
        }
        //System.out.println("Using maps from "+(new File(".")).getCanonicalPath()+mapsloc);
        FileReader fRdr = new FileReader(new File(mapsloc,file));
        //FileReader fRdr = new FileReader(new File("\\src\\maps\\terrain_group_5.txt"));
        Reader rdr = new NoiseFilterReader(fRdr);
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
       // System.out.println(tGraph.toString());
       //display(tGraph);

       assertEquals(copiedFromConsole2, tGraph.toString());

        //assertEquals(copiedFromConsole, tGraph.toString());


    }


    @Test
    public void testSolution() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap+terrain+paths+borders+solution));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load(); 
        //display(tGraph);
        //added by company 1 team 3 for story #68
        BareG g = tGraph;
        BareV start = g.getVertex(tGraph.getStart().getLinear());
        BareV goal  = g.getVertex(tGraph.getGoal().getLinear());
        List<Integer> result = PathFinder.findPath(g, start, goal);
        List<Integer> expectedSolution = new ArrayList<Integer>();
        expectedSolution.add(12);
        expectedSolution.add(7);
        expectedSolution.add(6);
        expectedSolution.add(10);
        expectedSolution.add(15);
        assertEquals(result, expectedSolution);
        
    }
    
    @Test 
    public void testBorders() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(
            new StringReader(minMap+terrain+paths+borders));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        //display(tGraph);
        String expected = "(12, 11: 900) "
        		+ "(12, 13: 195) "
        		+ "(12, 17: 900) "
        		+ "(12, 6: 900) "
        		+ "(12, 7: 165) "
        		+ "(12, 8: 130) "
        		+ "(13, 12: 195) "
        		+ "(13, 14: 195) "
        		+ "(13, 17: 900) "
        		+ "(13, 18: 900) "
        		+ "(13, 19: 195) "
        		+ "(13, 8: 195) "
        		+ "(18, 13: 900) "
        		+ "(18, 17: 195) "
        		+ "(18, 19: 143) "
        		+ "(18, 22: 1200) "
        		+ "(18, 23: 1200) "
        		+ "(18, 24: 195) "
        		+ "(24, 18: 195) "
        		+ "(24, 19: 195) "
        		+ "(24, 23: 1200) "
        		+ "(24, 29: 1200) ";
        String result = new String();
        final int[] border_x = {2, 3, 3, 4};
        final int[] border_y = {3, 4, 5, 6};
        for(int i = 0; i < 4; ++i) {
        	result += tGraph.getVertex(border_x[i], border_y[i]).toString();
        }

        assertEquals(expected, result);
    }
    
    @Test
    public void testPaths() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap+terrain+paths));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        //System.out.println(tGraph.toString());
        //display(tGraph);
        assertEquals(resForTestPaths, tGraph.toString());
    }
    
    @Test
    public void testTerrain() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap+terrain));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        int waterx[]={1,2};
        int watery[]={3,4};
        int expectedwaternumVal=190;
        String expectedwaterString="Water: \n Hint: Shhh quiet water, watch out!\n NOT gonna try to traverse with your fancy car TAT.";
        
        int brushx[]={0,0,1,1,2,3};
        int brushy[]={4,5,5,6,6,7};
        int expectedbrushnumVal=180;
        String expectedbrushString="Brush: \n Hint: Okay to traverse, but with low obstacles.\n Go explore the wild!";
        
        int forestx[]={3,3,3,4,4,4};
        int foresty[]={3,4,5,4,5,6};
        int expectedforestnumVal=195;
        String expectedforestString="Forest: \n Hint: All trees!\n Super hard to traverse with your beloved car but you may walk and embrace the nature!";
        
        for(int i=0;i<waterx.length;++i){
        	TerrainType cellType = tGraph.getVertex(waterx[i],watery[i]).getData().getTerrain();
        	assertEquals(cellType.getNumVal(),expectedwaternumVal);
        	assertEquals(cellType.getDescription(),expectedwaterString);
        }
        for(int i=0;i<brushx.length;++i){
        	TerrainType cellType = tGraph.getVertex(brushx[i],brushy[i]).getData().getTerrain();
        	assertEquals(cellType.getNumVal(),expectedbrushnumVal);
        	assertEquals(cellType.getDescription(),expectedbrushString);
        }
        for(int i=0;i<forestx.length;++i){
        	TerrainType cellType = tGraph.getVertex(forestx[i],foresty[i]).getData().getTerrain();
        	assertEquals(cellType.getNumVal(),expectedforestnumVal);
        	assertEquals(cellType.getDescription(),expectedforestString);
        }
        
    }
    
    @Test
    public void testMin() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap));
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        
        // added by company 1 team 3
        BareG g = tGraph;
        BareV start = g.getVertex(tGraph.getStart().getLinear());
        BareV goal  = g.getVertex(tGraph.getGoal().getLinear());
        List<Integer> result = PathFinder.findPath(g, start, goal);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(12);
        expected.add(11);
        expected.add(15);
        //System.out.println("===================testMin====================");
        //System.out.println(expected);
        //System.out.println(result);
        //System.out.println("===================testMin====================");
        assertEquals(expected, result);
        //display(tGraph);
    }
    


    public void display(TerrainGraph g){
        TerrainBoard m = new TerrainBoard(g);
        int w = g.width;
        int h = g.height;
        m.setPreferredSize(new Dimension(w*144,h*144));
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane jsp = new JScrollPane( m );
        f.setContentPane(jsp);
        f.setVisible(true);
//        f.revalidate();
//        f.repaint();
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
    }

}
