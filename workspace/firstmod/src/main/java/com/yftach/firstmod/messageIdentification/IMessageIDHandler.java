package com.yftach.firstmod.messageIdentification;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IMessageIDHandler {
	String getID();
	void setID(String id);
}
