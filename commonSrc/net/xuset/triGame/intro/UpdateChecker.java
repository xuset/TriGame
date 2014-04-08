package net.xuset.triGame.intro;


import net.xuset.objectIO.util.AsynchronousUrlReader;
import net.xuset.triGame.Params;
import net.xuset.triGame.Version;
import net.xuset.triGame.Version.VersionFormatException;

public class UpdateChecker {
	private final String updateGitUrl =
			"https://raw.github.com/xuset/TriGame/master/version";
	private final String updateXusetUrl =
			"http://www.xuset.net/polyDefense/version";
	
	private final String gitUrl = "github.com/xuset/triGame/releases";
	private final String xusetUrl = "www.xuset.net";
	
	private final AsynchronousUrlReader gitReader, xusetReader;
	
	private String url = "";
	
	
	public UpdateChecker() {
		if (Params.CHECK_FOR_UPDATES) {
			gitReader = new AsynchronousUrlReader(updateGitUrl, 10);
			xusetReader = new AsynchronousUrlReader(updateXusetUrl, 10);
		} else {
			gitReader = null;
			xusetReader = null;
		}
	}

	public boolean isFinished() {
		if (!Params.CHECK_FOR_UPDATES)
			return false;
		return isFinished(gitReader) || isFinished(xusetReader);
	}
	
	private boolean isFinished(AsynchronousUrlReader reader) {
		return reader.isFinished() && !reader.encounteredError();
	}

	public boolean updateAvailable() {
		if (!Params.CHECK_FOR_UPDATES || !isFinished())
			return false;
		
		if (isFinished(xusetReader))
			return checkUpdates(xusetReader.getContents(), xusetUrl);
		if (isFinished(gitReader))
			return checkUpdates(gitReader.getContents(), gitUrl);
		
		return false;
	}
	
	private boolean checkUpdates(String content, String url) {
		content = content.trim();
		try {
			Version newestVersion = new Version(content);
			if (newestVersion.isGreaterThan(Params.VERSION)) {
				this.url = url;
				return true;
			}
			return false;
		} catch (VersionFormatException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public String getUpdateURL() {
		return url;
	}
}
