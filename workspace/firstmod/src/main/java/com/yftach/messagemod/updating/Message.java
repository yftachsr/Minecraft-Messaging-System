package com.yftach.messagemod.updating;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	@Override
	public String toString() {
		return "{uuid: " + this.playerUUID + ", id: " + this._id + 
				", x: " + this.x + ", y: " + this.y + ", z: " + this.z
				+ ", text: " + this.text + ", dir: " + this.dir + "}";
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31).append(_id).toHashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Message))
			return false;
		Message message = (Message) o;
		return new EqualsBuilder().append(_id, message._id).isEquals();

	}
	
	public String getText() {
		return text;
	}
	
	public int getLikes() {
		return likes;
	}
	
	
}
