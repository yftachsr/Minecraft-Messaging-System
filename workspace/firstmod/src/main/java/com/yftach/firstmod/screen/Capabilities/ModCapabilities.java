package com.yftach.firstmod.screen.Capabilities;

import static net.minecraftforge.common.capabilities.CapabilityManager.get;

import com.yftach.firstmod.messageIdentification.IMessageIDHandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ModCapabilities extends ForgeCapabilities{
	public static final Capability<IMessageIDHandler> ID_HANDLER = get(new CapabilityToken<>(){});
}
