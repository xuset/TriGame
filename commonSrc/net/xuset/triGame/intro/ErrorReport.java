package net.xuset.triGame.intro;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.xuset.triGame.Params;

public class ErrorReport {
	private final String report;
	
	public ErrorReport(Exception ex) {
		report = createReport(ex);
	}
	
	private String createReport(Exception ex) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Erorr class: " + ex.toString() + "\n");
		builder.append("Error message: " + ex.getMessage() + "\n");
		builder.append("Local message: " + ex.getLocalizedMessage() + "\n");
		builder.append("PolyDefense version: " + Params.VERSION + "\n");
		builder.append("OS: " + System.getProperty("os.name") + "\n");
		builder.append("OS version: " + System.getProperty("os.version") + "\n");
		builder.append("OS arch: " + System.getProperty("os.arch") + "\n");
		builder.append("Java version: " + System.getProperty("java.runtime.version") + "\n");
		builder.append("OpenGL: " + System.getProperty("sun.java2d.opengl") + "\n");
		builder.append("D3D: " + System.getProperty("sun.java2d.d3d") + "\n");
		builder.append("Stacktrace:\n");
		
		for (StackTraceElement element : ex.getStackTrace()) {
			builder.append("      at " + element.toString() + "\n");
		}
		
		return builder.toString();
	}
	
	public String getReport() { return report; }
	
	public boolean postReport(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes("data=" + URLEncoder.encode(report, "UTF-8"));
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			con.disconnect();
			return responseCode == 200;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
