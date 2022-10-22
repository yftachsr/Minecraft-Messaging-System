package com.yftach.firstmod.minecraftNetworking;

import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.minecraftNetworking.packet.MessageC2SPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
	private static SimpleChannel CHANNEL;
	private static int packetId = 0;
	
	private static int id() {
		return packetId++;
	}
	
	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(FirstMod.MOD_ID, "messages"))
				.networkProtocolVersion(() -> "1.0")
				.clientAcceptedVersions(s -> true)
				.serverAcceptedVersions(s -> true)
				.simpleChannel();
		
		CHANNEL = net;
		
		net.messageBuilder(MessageC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(MessageC2SPacket::new)
				.encoder(MessageC2SPacket::toBytes)
				.consumerMainThread(MessageC2SPacket::handle)
				.add();
	}
	
	public static <MSG> void sendToServer(MSG message) {
		CHANNEL.sendToServer(message);
	}
	
	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
	
}
