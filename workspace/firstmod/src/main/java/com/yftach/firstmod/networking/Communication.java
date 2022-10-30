package com.yftach.firstmod.networking;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.JsonObject;

public class Communication {
	
	/**
	 * Sends a GET request to the specified end point
	 * @param endPoint
	 * @return - Response received
	 */
	public static HttpResponse<String> getReq(String endPoint) {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endPoint))
	            .header("Content-Type", "application/json")
	            .GET()
	            .build();
	            
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
//			System.out.println("status:" + response.statusCode());
//	        System.out.println("response:\n" + response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        
        return response;
	}
	
	/**
	 * Send a POST request to the specified end point
	 * @param endPoint
	 * @param data - String in JSON format to send
	 * @return - Response received
	 */
	public static HttpResponse<String> postReq(String endPoint, String data) {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endPoint))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
	            .build();
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			System.out.println("status:" + response.statusCode());
	        System.out.println("response:\n" + response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        
        return response;
	}
	
	/**
	 * Sends a PUT request to the specified end point
	 * @param endPoint
	 * @param - String in JSON format to send
	 * @return - Response received
	 */
	public static HttpResponse<String> putReq(String endPoint, String data) {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endPoint))
	            .header("Content-Type", "application/json")
	            .PUT(HttpRequest.BodyPublishers.ofString(data.toString()))
	            .build();
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			System.out.println("status:" + response.statusCode());
	        System.out.println("response:\n" + response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        
        return response;
	}
}
