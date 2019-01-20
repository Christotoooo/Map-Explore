package cn.edu.pku.sei;

import java.io.File;
import java.net.URL;

public class PopHelppage {
	public static void popPage(String page)
	{
		try 
		{
			System.out.println("here");
			File file=new File(page);
			URL url=file.toURL();
//			File tmpfile = File.createTempFile("temp", ".html");
//			BufferedWriter bw = new BufferedWriter(new FileWriter(tmpfile));
//			bw.write("<html><head><meta http-equiv=\"refresh\" content=\"0;url="+
//					 url.toString()+anchor+"\" /></head></html>");
//			bw.close();
			String sys=System.getProperty("os.name").toLowerCase();
			if(sys.indexOf("mac")!=-1)
			{
				Runtime.getRuntime().exec(new String[]{"/usr/bin/open",file.getAbsolutePath()});
			}
			else
			{
				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
					dp.browse(url.toURI());
					
				}
			}
		}
		catch (Exception e1) {
				e1.printStackTrace();
		}
	}
}
