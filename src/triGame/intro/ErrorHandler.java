package triGame.intro;

class ErrorHandler {
	public static interface Task { void run(); }
	
	private final Task task;
	
	ErrorHandler(Task task) {
		this.task = task;
	}
	
	void startTask() {
		try {
			task.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorWindow window = new ErrorWindow(ex);
			window.waitForDisposal();
			System.exit(1);
		}
	}
}
