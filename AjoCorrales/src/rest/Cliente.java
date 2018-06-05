package rest;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Cliente {

	public static String getUsuarios() {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		return response.readEntity(String.class);
	}

	public static void imprimirPrettyJson(String output) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(output);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);
	}

	public static void main(String[] args) {
		int i = 0;
		while (i != -1) {
			try {
				Scanner sc = new Scanner(System.in);

				System.out.println("Elige opción: ");
				i = sc.nextInt();

				switch (i) {
				case -1:
					System.out.println("Hasta pronto");
					break;
				case 0:
					System.out.println("CERO");
					break;
				case 1:
					imprimirPrettyJson(getUsuarios());
					break;

				default:
					System.out.println("DEFAULT");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
