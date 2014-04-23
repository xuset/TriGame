package net.xuset.triGame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.xuset.triGame.game.ui.IBrowserOpener;

public class AndroidBrowserOpener implements IBrowserOpener {
	private final Context context;

	public AndroidBrowserOpener(Context context) {
		this.context = context;
	}
	
	@Override
	public void openBrowser(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}

}
