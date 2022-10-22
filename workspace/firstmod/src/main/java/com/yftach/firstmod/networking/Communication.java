package com.yftach.firstmod.networking;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.JsonObject;

public class Communication {
	
	private String endPoint;
	
	public Communication(String address) {
		this.endPoint = address;
	}
	
	public HttpResponse<String> getReq() {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endPoint))
	            .header("Content-Type", "application/json")
	            .GET()
	            .build();
	            
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        System.out.println("status:" + response.statusCode());
        System.out.println("response:\n" + response.body());
        return response;
	}
	
	public HttpResponse<String> postReq(JsonObject data) {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endPoint))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
	            .build();
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        System.out.println("status:" + response.statusCode());
        System.out.println("response:\n" + response.body());
        return response;
	}
}
