package com.yftach.firstmod.item;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MessagingCrystal extends Item{

	public MessagingCrystal(Properties p_41383_) {
		super(p_41383_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)  {
		
		if(world.isClientSide())
			return super.use(world, player, hand);
		
		String postEndpoint = "http://localhost:3000";
		
        String inputJson = "{ \"playerName\": \"" + player.getName().toString() + "\"" +
        		", \"x\": \"" + player.getX() + "\"" +
        		", \"y\": \"" + player.getY() + "\"" +
        		", \"z\": \"" + player.getZ() + "\" }";
 
       
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(postEndpoint))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(inputJson))
	            .build();
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        System.out.println("status:" + response.statusCode());
        System.out.println("response:\n" + response.body());
		
		return super.use(world, player, hand);
		
		
	}
}
