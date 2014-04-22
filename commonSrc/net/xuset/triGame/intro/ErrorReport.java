package net.xuset.triGame.intro;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.xuset.triGame.Params;

public class ErrorReport {
	private final String environment;
	private final String stackTrace;
	
	public ErrorReport(Exception ex) {
		environment = createEnviromentReport();
		stackTrace = createStackTraceReport(ex);
	}
	
	private String createEnviromentReport() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("PolyDefense version: " + Params.VERSION + "\n");
		builder.append("OS: " + System.getProperty("os.name") + "\n");
		builder.append("OS version: " + System.getProperty("os.version") + "\n");
		builder.append("OS arch: " + System.getProperty("os.arch") + "\n");
		builder.append("Java version: " + System.getProperty("java.runtime.version") + "\n");
		builder.append("OpenGL: " + System.getProperty("sun.java2d.opengl") + "\n");
		builder.append("D3D: " + System.getProperty("sun.java2d.d3d") + "\n");
		
		return builder.toString();
	}
	
	private String createStackTraceReport(Exception ex) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Stacktrace: "  + ex.toString() + "\n");
		for (StackTraceElement element : ex.getStackTrace()) {
			builder.append(" - at " + element.toString() + "\n");
		}
		
		return builder.toString();
	}
	
	public boolean postReport(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes("environment=" + URLEncoder.encode(environment, "UTF-8"));
			wr.writeBytes("&stackTrace=" + URLEncoder.encode(stackTrace, "UTF-8"));
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
