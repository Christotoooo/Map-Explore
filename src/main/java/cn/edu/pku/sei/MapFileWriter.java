package cn.edu.pku.sei;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapFileWriter {

	public MapFileWriter(String mapInfo, File mapFile) {
		try {
			FileWriter wr = new FileWriter(mapFile);
			BufferedWriter br = new BufferedWriter(wr);
			br.write(mapInfo);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
