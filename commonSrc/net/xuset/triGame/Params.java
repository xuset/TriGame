package net.xuset.triGame;

import java.util.logging.Level;

public class Params {
	
	/*
	 * To create a 'release ready' config:
	 *   - Set IS_ANDROID to true for an Android config, false otherwise
	 *   - Set DEBUG_MODE to false
	 *     - This should set CHECK_FOR_UPDATES to true (unless on Android)
	 *   - Add the key for SCORE_KEY
	 *   - Set the correct VERSION
	 *   - Change contents of Version file if necessary
	 *   - It is crucial that UNUSED remain null!
	 */
	
	public static final boolean IS_ANDROID = false;
	public static final boolean DEBUG_MODE = true;
	public static final boolean CHECK_FOR_UPDATES = !DEBUG_MODE && !IS_ANDROID;
	public static final boolean ALLOW_DOUBLE_CLICK = !IS_ANDROID;
	public static final boolean ENABLE_MULTIPLAYER = !IS_ANDROID;
	
	public static final Version VERSION = new Version(1, 3, 0);
	public static final Level LOG_LEVEL = Level.WARNING;
	
	public static final String GAME_NAME = "PolyDefense";
	public static final String SCORE_KEY = "no esta aqui";

	public static final String SCORE_SUBMIT_URL =
			"http://xuset.net/polyDefense/scores/scoreSubmit.php";
	public static final String ERROR_REPORT_URL =
			"http://www.xuset.net/polyDefense/bugs/submit.php";
	
	public static final String UNUSED = null; //Only used for irony.
}
