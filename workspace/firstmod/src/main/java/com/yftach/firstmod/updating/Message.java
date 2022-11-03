package com.yftach.firstmod.updating;


public class Message extends ModSchema{
	
	private String text;
	
	public Message(String playerUUID, String playerName, double x, double y, double z,
			String text, int dir) {
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.text = text;
		this.dir = dir;
	}
	
	public String getText() {
		return text;
	}
}
