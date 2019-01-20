package cn.edu.pku.sei.drivers;

import cn.edu.pku.sei.MapSelection;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMapSelection {
	//private static MapSelection maps;
	public String arg = "";
	
	@Test
	/*
	 * testMapSelection by group 3
	 */
	public void testMapSelection() throws IOException, InterruptedException {
		String[] args = {arg};
		final MapSelection t = new MapSelection();
		final List<String> mapList = getAllFile();
		final String[] maps = mapList.toArray(new String[mapList.size()]);
		final StringBuilder s = new StringBuilder();
		for(final String map:maps){
			s.append(map);
		}
		assertEquals(t.toString(),s.toString());

	}

	private static List<String> getAllFile() {
		final List<String> list = new ArrayList<String>();
		final File baseFile = new File("src/maps");
		if (baseFile.isFile() || !baseFile.exists()) {
			return list;
		}
		final File[] files = baseFile.listFiles();
		for (final File file : files) {
			if (file.isDirectory()) {
				//list.addAll(getAllFile(file.getAbsolutePath()));
			} else {
				list.add(file.getName());
			}
		}
		return list;
	}

}
