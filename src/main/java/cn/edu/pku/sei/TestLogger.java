package cn.edu.pku.sei;

public class TestLogger implements Logger {

	private String buffer = "";
	
	@Override
	public void log(String data) {
	     buffer += data;		
	}
	
	
	public String toString() {
		return buffer;
	}
	

}
