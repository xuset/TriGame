package triGame.intro;

import tSquare.system.Network;
import triGame.game.GameMode.GameType;

public class GameInfo {
	public enum NetworkType { SOLO, HOST, JOIN }

	NetworkType networkType = NetworkType.SOLO;
	Network network = null;
	GameType mode = GameType.SURVIVAL;
	
	public Network getNetwork() { return network; }
	public GameType getGameType() { return mode; }
	public NetworkType getNetworkType() { return networkType; }
	
	public GameInfo(Network network, GameType gameType, NetworkType networkType) {
		this.network = network;
		this.mode = gameType;
		this.networkType = networkType;
	}
	
	GameInfo() {
		
	}
}
