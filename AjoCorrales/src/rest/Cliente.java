package rest;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Cliente {

	public static void main(String[] args) {
		int i = 0;
		Scanner sc = new Scanner(System.in);
		while (i != -1) {
			try {
				System.out.println("Elige opción: ");
				System.out.println(
						"USUARIOS: \n   1.Obtener todos los usuarios \n   2.Obtener un usuario determinado \n   3.Añadir un usuario \n   4.Actualizar perfil \n   5.Borrar perfil");
				System.out.println(
						"AMIGOS: \n   6.Agregar un amigo \n   7.Borrar un amigo \n   8.Ver mis amigos \n   9.Buscar usuarios por patrón \n   10.Buscar mis amigos por patrón");
				System.out.println(
						"PUBLICACIONES: \n   11.Añadir Publicación \n   12. \n   13. \n   14.Buscar publicaciones de un usuario \n ");
				System.out.println("APPMOVIL: \n 15. Ver aplicación web para un usuario \n");
				try {
					i = Integer.parseInt(sc.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Introduzca un número de la lista");
					continue;
				}
				// sc.nextLine();
				switch (i) {
				case -1:
					System.out.println("Hasta pronto");
					break;
				case 1:
					System.out.println(imprimirPrettyJson(getUsuarios()));
					break;
				case 2:
					System.out.println("Introduce usuario a buscar");
					String usuario2 = sc.nextLine();
					System.out.println(imprimirPrettyJson(getUsuario(usuario2)));
					break;
				case 3:
					System.out.println("Introduce nickname del usuario a insertar");
					String usuario3 = sc.nextLine();
					System.out.println("Introduce nombre completo");
					String nombreCompleto3 = sc.nextLine();
					System.out.println("Introduce país");
					String pais3 = sc.nextLine();
					System.out.println("Introduce fecha de nacimiento (dd/mm/aaaa)");
					String fechaNacimiento3 = sc.nextLine();
					System.out.println("Introduce correo electrónico");
					String correo3 = sc.nextLine();
					System.out.println(imprimirPrettyJson(
							anadirUsuario(usuario3, nombreCompleto3, pais3, fechaNacimiento3, correo3)));
					break;
				case 4:
					System.out.println("Introduce nickname del usuario a modificar");
					String usuario4 = sc.nextLine();
					System.out.println("Introduce nuevo nombre completo");
					String nombreCompleto4 = sc.nextLine();
					System.out.println("Introduce nuevo país");
					String pais4 = sc.nextLine();
					System.out.println("Introduce nueva fecha de nacimiento (dd/mm/aaaa)");
					String fechaNacimiento4 = sc.nextLine();
					System.out.println("Introduce nuevo correo electrónico");
					String correo4 = sc.nextLine();
					System.out.println(imprimirPrettyJson(
							actualizarPerfil(usuario4, nombreCompleto4, pais4, fechaNacimiento4, correo4)));
					break;
				case 5:
					System.out.println("Introduce tu nickname para borrar el perfil");
					String usuario5 = sc.nextLine();
					System.out.println(imprimirPrettyJson(borrarPerfil(usuario5)));
					break;
				case 6:
					System.out.println("Introduce tu nickname");
					String usuario6 = sc.nextLine();
					System.out.println("Introduce el nickname de tu amigo");
					String amigo6 = sc.nextLine();
					System.out.println(imprimirPrettyJson(anadirAmigo(usuario6, amigo6)));
					break;
				case 7:
					System.out.println("Introduce tu nickname");
					String usuario7 = sc.nextLine();
					System.out.println("Introduce el nickname del amigo a borrar");
					String amigo7 = sc.nextLine();
					System.out.println(imprimirPrettyJson(borrarAmigo(usuario7, amigo7)));
					break;
				case 8:
					System.out.println("Introduce tu nickname");
					String usuario8 = sc.nextLine();
					System.out.println(imprimirPrettyJson(getAmigos(usuario8)));
					break;
				case 9:
					System.out.println("Introduce tu nickname");
					String usuario9 = sc.nextLine();
					System.out.println("Introduce el patron para buscar usuario");
					String patron9 = sc.nextLine();
					System.out.println(imprimirPrettyJson(buscarAmigos(usuario9, patron9)));
					break;
				case 10:
					System.out.println("Introduce tu nickname");
					String usuario10 = sc.nextLine();
					System.out.println("Introduce el patron para buscar usuario");
					String patron10 = sc.nextLine();
					System.out.println(imprimirPrettyJson(getAmigosPatron(usuario10, patron10)));
					break;
				case 11:
					System.out.println("Introduce el título de la publicación(idPublicacion)");
					String idPublicacion11 = sc.nextLine();
					System.out.println("Introduce el propietario de la publicación(nickname)");
					String usuario11 = sc.nextLine();
					System.out.println("Introduce el texto a escribir(tweet)");
					String tweet11 = sc.nextLine();
					System.out.println(imprimirPrettyJson(añadirPublicacion(idPublicacion11, usuario11, tweet11)));
					break;
				case 12:
					break;
				case 13:

					break;
				case 14:
					System.out.println("Introduce el nickname de las publicaciones");
					String usuario14 = sc.nextLine();
					System.out.println("Introduce el patron para filtrar publicaciones");
					String patron14 = sc.nextLine();
					System.out.println(imprimirPrettyJson(buscarpublicaciones(usuario14, patron14)));
					break;
				case 15:
					System.out.println("Introduce el nickname");
					String usuario12 = sc.nextLine();
					System.out.println(imprimirPrettyJson(getAppMovil(usuario12)));
					break;
				case 16:

					break;
				case 17:

					break;
				case 18:

					break;
				case 19:

					break;
				case 20:

					break;
				case 21:

					break;
				case 22:

					break;
				default:
					System.out.println("Opcion incorrecta.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (sc != null)
			sc.close();
	}

	public static String getUsuarios() {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String getUsuario(String usuario) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + usuario);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String anadirUsuario(String nickname, String nombreCompleto, String pais, String fechaNacimiento,
			String correo) {

		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios");
		Usuario user = new Usuario(nickname, nombreCompleto, pais, fechaNacimiento, correo);

		Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.xml(user));

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String actualizarPerfil(String nickname, String nombreCompleto, String pais, String fechaNacimiento,
			String correo) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname);
		Usuario user = new Usuario(nickname, nombreCompleto, pais, fechaNacimiento, correo);

		Response response = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.xml(user));

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String borrarPerfil(String nickname) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String anadirAmigo(String nickname, String nickAmigo) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/amigos");
		Usuario amigo = new Usuario(nickAmigo);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.xml(amigo));

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String borrarAmigo(String nickname, String nickAmigo) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/" + nickAmigo);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String getAmigos(String nickname) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/amigos");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String buscarAmigos(String nickname, String patron) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/" + patron);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String getAmigosPatron(String nickname, String patron) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/amigos/" + patron);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String buscarpublicaciones(String nickname, String patron) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(
				"http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/amigos/publicaciones/" + patron);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String añadirPublicacion(String idPublicacion, String propietario, String tweet) {

		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AjoCorrales/upmsocial/publicaciones/" + propietario);
		Publicacion pub = new Publicacion(idPublicacion, propietario, tweet);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.xml(pub));

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String getAppMovil(String nickname) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://localhost:8080/AjoCorrales/upmsocial/usuarios/" + nickname + "/appmovil");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Petición fallida : código de error HTTP : " + response.getStatus());
		}
		return response.readEntity(String.class);
	}

	public static String imprimirPrettyJson(String output) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(output);
		String prettyJsonString = gson.toJson(je);

		return prettyJsonString;
	}
}
