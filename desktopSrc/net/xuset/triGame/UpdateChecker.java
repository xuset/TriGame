package net.xuset.triGame;


import net.xuset.objectIO.util.AsynchronousUrlReader;
import net.xuset.triGame.Params;
import net.xuset.triGame.intro.IUpdateChecker;

public class UpdateChecker implements IUpdateChecker {
	private final String updateUrl = "https://raw.github.com/xuset/TriGame/master/version";
	private final AsynchronousUrlReader reader;
	
	
	public UpdateChecker() {
		reader = new AsynchronousUrlReader(updateUrl, 10);
	}

	@Override
	public boolean isFinished() {
		return reader.isFinished() && !reader.encounteredError();
	}

	@Override
	public boolean updateAvailable() {
		String contents = reader.getContents().trim();
		return !contents.equals(Params.VERSION);
	}
}
