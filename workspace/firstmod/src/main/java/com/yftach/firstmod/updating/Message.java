package com.yftach.firstmod.updating;


public class Message extends ModSchema {
	
	private String text;
	private int likes;
	
	public Message(String playerUUID, String _id, double x, double y, double z,
			String text, int dir, int likes) {
		this.playerUUID = playerUUID;
		this._id = _id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.text = text;
		this.dir = dir;
		this.likes = likes;
	}
	
	public Message(String playerUUID, double x, double y, double z, String text, int dir, int likes) {
		this.playerUUID = playerUUID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.text = text;
		this.dir = dir;
		this.likes = likes;
	}
	
	public String toString() {
		return "{uuid: " + this.playerUUID + ", id: " + this._id + 
				", x: " + this.x + ", y: " + this.y + ", z: " + this.z
				+ ", text: " + this.text + ", dir: " + this.dir + "}";
	}
	
	public String getText() {
		return text;
	}
	
	public int getLikes() {
		return likes;
	}
}
