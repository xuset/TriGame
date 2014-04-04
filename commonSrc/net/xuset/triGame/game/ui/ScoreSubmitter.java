package net.xuset.triGame.game.ui;

import java.security.MessageDigest;

import net.xuset.tSquare.math.IdGenerator;
import net.xuset.triGame.Params;

public class ScoreSubmitter {
	private final long salt = IdGenerator.getNext();
	
	ScoreSubmitter() {
		
	}
	
	public String craftUrl(int roundNumber, int playerCount) {
		String hash = createHash(roundNumber, playerCount);
		
		StringBuilder url = new StringBuilder();
		url.append(Params.SCORE_SUBMIT_URL).
				append("?salt=").append(salt).
				append("&roundNumber=").append(roundNumber).
				append("&playerCount=").append(playerCount).
				append("&hash=").append(hash);
		return url.toString();
	}
	
	public String createHash(int roundNumber, int playerCount) {
		StringBuilder preHash = new StringBuilder();
		preHash.append(salt).append(roundNumber).append(playerCount).
				append(Params.SCORE_KEY);
		return toSha256(preHash.toString());
	}
	
	private static String toSha256(String base) {
		try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
