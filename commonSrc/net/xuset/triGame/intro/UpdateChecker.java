package net.xuset.triGame.intro;


import net.xuset.objectIO.util.AsynchronousUrlReader;
import net.xuset.triGame.Params;
import net.xuset.triGame.Version;

public class UpdateChecker {
	private final String updateUrl = "https://raw.github.com/xuset/TriGame/master/version";
	private final AsynchronousUrlReader reader;
	
	
	public UpdateChecker() {
		if (Params.CHECK_FOR_UPDATES)
			reader = new AsynchronousUrlReader(updateUrl, 10);
		else
			reader = null;
	}

	public boolean isFinished() {
		if (reader == null)
			return false;
		return reader.isFinished() && !reader.encounteredError();
	}

	public boolean updateAvailable() {
		if (reader == null)
			return false;
		
		String contents = reader.getContents().trim();
		Version newestVersion = new Version(contents);
		
		return newestVersion.isGreaterThan(Params.VERSION);
	}
}
