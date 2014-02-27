package net.xuset.triGame.game;

import net.xuset.tSquare.system.Network;
import net.xuset.triGame.game.GameMode.GameType;


public class GameInfo {
	public enum NetworkType { SOLO, HOST, JOIN }

	private final NetworkType networkType;
	private final Network network;
	private final GameType mode;
	
	public Network getNetwork() { return network; }
	public GameType getGameType() { return mode; }
	public NetworkType getNetworkType() { return networkType; }
	
	public GameInfo(Network network, GameType gameType, NetworkType networkType) {
		this.network = network;
		this.mode = gameType;
		this.networkType = networkType;
	}
}
