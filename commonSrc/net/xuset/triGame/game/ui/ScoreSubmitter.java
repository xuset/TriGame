package net.xuset.triGame.game.ui;

import net.xuset.objectIO.netObj.NetVar;
import net.xuset.tSquare.math.IdGenerator;
import net.xuset.triGame.Hasher;
import net.xuset.triGame.Params;

public class ScoreSubmitter {
	private final long salt = IdGenerator.getNext();
	private final NetVar.nLong gameId;
	
	public ScoreSubmitter(NetVar.nLong gameId) {
		this.gameId = gameId;
	}
	
	public String craftUrl(int roundNumber, int playerCount) {
		String hash = createHash(roundNumber, playerCount);
		
		StringBuilder url = new StringBuilder();
		url.append(Params.SCORE_SUBMIT_URL).
				append("?salt=").append(salt).
				append("&roundNumber=").append(roundNumber).
				append("&playerCount=").append(playerCount).
				append("&gameId=").append(gameId.get().longValue()).
				append("&hash=").append(hash);
		return url.toString();
	}
	
	public String createHash(int roundNumber, int playerCount) {
		StringBuilder preHash = new StringBuilder();
		preHash.append(salt).append(roundNumber).append(playerCount).
				append(gameId.get().longValue()).append(Params.SCORE_KEY);
		return Hasher.hash(preHash.toString());
	}
}
