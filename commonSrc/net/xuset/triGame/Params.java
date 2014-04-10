package net.xuset.triGame;

import java.util.logging.Level;

public class Params {
	
	/*
	 * To create a 'release ready' config:
	 *   - Set DEBUG_MODE to false
	 *     - This should set CHECK_FOR_UPDATES to true
	 *   - Add the key for SCORE_KEY
	 *   - It is crucial that UNUSED remain null!
	 */
	
	public static boolean DEBUG_MODE = true;
	public static final String WEBSITE_URL = "http://wwww.xuset.net";
	public static final String GITHUB_URL = "https://github.com/xuset/triGame";
	public static final String GAME_NAME = "PolyDefense";
	public static final Version VERSION = new Version(1, 2, 0);
	public static final boolean CHECK_FOR_UPDATES = !DEBUG_MODE;
	public static final String UNUSED = null; //Only used for irony.
	public static final String SCORE_SUBMIT_URL =
			"http://xuset.net/polyDefense/scores/scoreSubmit.php";
	public static final String SCORE_KEY = "no esta aqui";
	public static final Level LOG_LEVEL = Level.WARNING;
	public static final String ERROR_REPORT_URL =
			"http://www.xuset.net/polyDefense/bugs/submit.php";
}
