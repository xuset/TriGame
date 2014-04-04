package net.xuset.triGame;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import net.xuset.triGame.game.ui.IBrowserOpener;

public class DesktopBrowserOpener implements IBrowserOpener {

	@Override
	public void openBrowser(String url) {
		openWebpage(url);
	}
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static void openWebpage(String url) {
	    try {
	        openWebpage(new URL(url).toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    	
	    }
	}

}
