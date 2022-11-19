package com.yftach.messagemod.minecraftNetworking;

import com.yftach.messagemod.MessagingSystemMod;
import com.yftach.messagemod.minecraftNetworking.packets.UpdateModBlockC2SPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
	 private static SimpleChannel INSTANCE;

	    private static int packetId = 0;
	    private static int id() {
	        return packetId++;
	    }

	    public static void register() {
	        SimpleChannel net = NetworkRegistry.ChannelBuilder
	                .named(new ResourceLocation(MessagingSystemMod.MOD_ID, "messages"))
	                .networkProtocolVersion(() -> "1.0")
	                .clientAcceptedVersions(s -> true)
	                .serverAcceptedVersions(s -> true)
	                .simpleChannel();

	        INSTANCE = net;

	        net.messageBuilder(UpdateModBlockC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
	                .decoder(UpdateModBlockC2SPacket::new)
	                .encoder(UpdateModBlockC2SPacket::toBytes)
	                .consumerMainThread(UpdateModBlockC2SPacket::handle)
	                .add();
	    }

	    public static <MSG> void sendToServer(MSG message) {
	         INSTANCE.sendToServer(message);
	    }

	    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
	        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	    }
}
