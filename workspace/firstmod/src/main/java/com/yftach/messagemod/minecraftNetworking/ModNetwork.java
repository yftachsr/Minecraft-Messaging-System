package com.yftach.messagemod.minecraftNetworking;

import com.yftach.messagemod.MessagingSystemMod;
import com.yftach.messagemod.minecraftNetworking.packets.UpdateMessageBlockC2SPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
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

        net.messageBuilder(UpdateMessageBlockC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateMessageBlockC2SPacket::new)
                .encoder(UpdateMessageBlockC2SPacket::toBytes)
                .consumerMainThread(UpdateMessageBlockC2SPacket::handle)
                .add();
    }

    public static <PACKET> void sendToServer(PACKET message) {
         INSTANCE.sendToServer(message);
    }

    public static <PACKET> void sendToPlayer(PACKET message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
