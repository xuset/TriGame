package net.xuset.triGame.intro;

public class DummyUpdateChecker implements IUpdateChecker {

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean updateAvailable() {
		return false;
	}

}
