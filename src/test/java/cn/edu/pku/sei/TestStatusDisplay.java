package cn.edu.pku.sei;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStatusDisplay{
	public String file = "/terrain_group_5.txt";
	@Test
	public void testStatusDisplay() {
		String[] args = {file};
		try {
			Explore.main(args);
		}catch(Exception e) {
			Explore.status.displayStatus("Status test");
		}
		
	}
}