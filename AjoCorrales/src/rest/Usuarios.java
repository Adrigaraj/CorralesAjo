package rest;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import bbdd.SentenciasSQL;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/usuarios")
public class Usuarios {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsuarios() {
		log.debug("Petición recibida en getUsuarios()");
		JSONArray objDevolver = new JSONArray();
		ResultSet rs = null;
		Usuario user = null;
		try {
			rs = SentenciasSQL.selectUsuarios();
			if (rs != null)
				while (rs.next()) {
					user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"), rs.getString("pais"),
							rs.getString("fechaNacimiento"), rs.getString("correo"), rs.getString("fechaAlta"));
					objDevolver.put(user.toJSON());
				}
			if (user != null)
				return new AppResponseJSONValue(Status.OK, null, objDevolver).toJtoString();
		} catch (SQLException e) {
			log.error("Error SQL: " + e.getErrorCode() + ". " + e.getMessage() + e.getStackTrace());
			return new AppResponseJSONValue(Status.INTERNAL_SERVER_ERROR, "Error al obtener los usuarios", null)
					.toJtoString();
		}
		return new AppResponseJSONValue(Status.OK, "No se han podido obtener usuarios. Puede que la BBDD esté vacía.",
				null).toJtoString();
	}

	@GET
	@Path("{nickname}")
	@Produces(MediaType.APPLICATION_JSON)
	public String verUsuario(@PathParam("nickname") String nickname) {
		log.debug("Petición recibida en getUsuario(nickname)");
		JSONArray objDevolver = new JSONArray();
		ResultSet rs = null;
		if (nickname == null || nickname.equals(""))
			return new AppResponse(Status.BAD_REQUEST, "El campo nickname está vacío", null).toJtoString();

		try {
			rs = SentenciasSQL.selectUsuario(nickname);
			if (rs != null && rs.next()) {
				Usuario user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"),
						rs.getString("pais"), rs.getString("fechaNacimiento"), rs.getString("correo"),
						rs.getString("fechaAlta"));
				objDevolver.put(user.toJSON());
				return new AppResponseJSONValue(Status.OK, null, objDevolver).toJtoString();
			}
		} catch (SQLException e) {
			log.error("Error SQL: " + e.getErrorCode() + ". " + e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.INTERNAL_SERVER_ERROR, "Error al obtener el usuario.", null).toJtoString();
		}
		return new AppResponse(Status.BAD_REQUEST, "No se ha encontrado el usuario", null).toJtoString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertarUsuario(JAXBElement<Usuario> user) {
		log.debug("Petición recibida en insertarUsuario(user)");
		int inserted = 0;

		Usuario us = user.getValue();
		String nickname = us.getNickname();
		String fechaAlta = new Date(new java.util.Date().getTime()).toString();
		us.setFechaAlta(fechaAlta);
		if (nickname == null)
			return new AppResponse(Status.OK, "Error al obtener el usuario", null).toJtoString();
		inserted = SentenciasSQL.insertUsuario(nickname, us.getNombreCompleto(), us.getPais(), us.getFechaNacimiento(),
				us.getCorreo(), fechaAlta);

		if (inserted == 1)
			return new AppResponse(Status.CREATED, null, us.toString()).toJtoString();
		if (inserted == 1062)
			return new AppResponse(Status.BAD_REQUEST, "Publicación ya introducida, cambie el idPublicacion", null)
					.toJtoString();
		if (inserted == 1452)
			return new AppResponse(Status.BAD_REQUEST,
					"El propietario de la publicación no está dado de alta en la BBDD", null).toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + inserted, null).toJtoString();
	}

	@POST
	@Path("/{nickname}/amigos")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public String agregarAmigo(@PathParam("nickname") String nickname, JAXBElement<Usuario> user) {
		log.debug("Petición recibida en agregarAmigo(user)");
		int inserted1 = 0;
		int inserted2 = 0;
		Usuario amigo = user.getValue();

		String nickAmigo = amigo.getNickname();

		if (nickname == null && nickAmigo == null)
			return new AppResponse(Status.BAD_REQUEST, "Error al obtener el usuario", null).toJtoString();
		inserted1 = SentenciasSQL.agregarAmigo(nickname, nickAmigo);
		inserted2 = SentenciasSQL.agregarAmigo(nickAmigo, nickname);

		if (inserted1 == 1 && inserted2 == 1)
			return new AppResponse(Status.CREATED, null, user.toString()).toJtoString();
		if (inserted1 == 1062 || inserted2 == 1062)
			return new AppResponse(Status.BAD_REQUEST, "Ya son amigos", null).toJtoString();
		if (inserted1 == 1452 || inserted2 == 1452)
			return new AppResponse(Status.BAD_REQUEST, "Alguno de los dos no esta en la BBDD", null).toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + inserted1, null).toJtoString();
	}

	@DELETE
	@Path("/{nickname}/{nickamigo}")
	@Produces(MediaType.APPLICATION_JSON)
	public String borrarAmigo(@PathParam("nickname") String nickname, @PathParam("nickamigo") String nickamigo) {
		log.debug("Petición recibida en borrarAmigo(nickname, nickamigo)");
		if (nickamigo == null || nickname == null)
			return new AppResponse(Status.BAD_REQUEST, "Campos nickamigo o nickname vacíos", null).toJtoString();

		int deleted1 = SentenciasSQL.borrarAmigo(nickamigo, nickname);
		int deleted2 = SentenciasSQL.borrarAmigo(nickname, nickamigo);
		if (deleted1 == 1 && deleted2 == 1)
			return new AppResponse(Status.OK, null, "Amigo borrado correctamente").toJtoString();
		if (deleted1 == 0 || deleted2 == 0)
			return new AppResponse(Status.BAD_REQUEST, "El nickname o el nickamigo no se han encontrado en la BBDD",
					null).toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + deleted1, null).toJtoString();
	}

	@GET
	@Path("/{nickname}/amigos")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAmigos(@PathParam("nickname") String nickname) {
		log.debug("Petición recibida en getAmigos()");
		JSONArray objDevolver = new JSONArray();
		ResultSet rs = null;
		Usuario user = null;
		try {
			rs = SentenciasSQL.selectAmigos(nickname);
			if (rs != null)
				while (rs.next()) {
					user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"), rs.getString("pais"),
							rs.getString("fechaNacimiento"), rs.getString("correo"), rs.getString("fechaAlta"));
					objDevolver.put(user.toJSON());
				}
			if (user != null)
				return new AppResponseJSONValue(Status.OK, null, objDevolver).toJtoString();
		} catch (SQLException e) {
			log.error("Error SQL: " + e.getErrorCode() + ". " + e.getMessage() + e.getStackTrace());
			return new AppResponseJSONValue(Status.INTERNAL_SERVER_ERROR,
					"Error al obtener los usuarios. SQLError: " + e.getErrorCode(), null).toJtoString();
		}
		return new AppResponseJSONValue(Status.BAD_REQUEST,
				"No se han podido obtener los amigos, comprueba el nickname.", null).toJtoString();
	}

	@GET
	@Path("/{nickname}/{amigo}")
	@Produces(MediaType.APPLICATION_JSON)
	public String buscarAmigos(@PathParam("nickname") String nickname, @PathParam("amigo") String patron) {

		log.debug("Petición recibida en buscarAmigos(nickname)");
		JSONArray objDevolver = new JSONArray();
		ResultSet rs = null;
		if (nickname == null || nickname.equals("") || patron.equals("") || patron == null) {
			return new AppResponse(Status.BAD_REQUEST, "No nick metido o no patron metido", null).toJtoString();
		}
		try {
			rs = SentenciasSQL.buscarAmigos(patron);
			while (rs != null && rs.next()) {
				Usuario us = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"), rs.getString("pais"),
						rs.getString("fechaNacimiento"), rs.getString("correo"), rs.getString("fechaAlta"));

				objDevolver.put(us.toJSON());
			}
			return new AppResponseJSONValue(Status.OK, null, objDevolver).toJtoString();
		} catch (SQLException e) {
			log.error("Error SQL: " + e.getErrorCode() + ". " + e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + e.getErrorCode(), null).toJtoString();
		}

	}

	@DELETE
	@Path("/{nickname}")
	@Produces(MediaType.APPLICATION_JSON)
	public String borrarPerfil(@PathParam("nickname") String nickname) {
		log.debug("Petición recibida en borrarPerfil(nickname)");
		if (nickname == null)
			return new AppResponse(Status.BAD_REQUEST, "Campo nickname vacío", null).toJtoString();

		int deleted1 = SentenciasSQL.borrarPerfil(nickname);

		if (deleted1 == 1)
			return new AppResponse(Status.OK, null, "Objeto borrado correctamente").toJtoString();
		if (deleted1 == 0)
			return new AppResponse(Status.NO_CONTENT, "El nickname no se han encontrado en la BBDD", null)
					.toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + deleted1, null).toJtoString();
	}

	@GET
	@Path("/{nickname}/amigos/{publicaciones}")
	@Produces(MediaType.APPLICATION_JSON)
	public String buscarEstadosContenido(@PathParam("nickname") String nickname,
			@PathParam("publicaciones") String patron) {

		log.debug("Petición recibida en buscarEstadosContenido(nickname)");
		JSONArray objDevolver = new JSONArray();
		ResultSet rs = null;
		if (nickname == null || nickname.equals("") || patron.equals("") || patron == null) {
			return new AppResponse(Status.BAD_REQUEST, "No nick metido o no patron metido", null).toJtoString();
		}
		try {

			rs = SentenciasSQL.buscarEstadosContenido(patron, nickname);
			while (rs != null && rs.next()) {
				Publicacion pub = new Publicacion(rs.getString("idPublicacion"), rs.getString("fechaPublicacion"),
						rs.getString("propietario"), rs.getString("tweet"));

				objDevolver.put(pub.toJSON());

			}
			return new AppResponseJSONValue(Status.OK, null, objDevolver).toJtoString();
		} catch (SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + e.getErrorCode(), null).toJtoString();
		}

	}

	@PUT
	@Path("/{nickname}")
	public String actualizarPerfil(@PathParam("nickname") String nickname, JAXBElement<Usuario> user) {
		log.debug("Petición recibida en actualizaPerfil");
		int updated = 0;
		Usuario us = user.getValue();
		try {
			if (nickname == null)
				return new AppResponse(Status.BAD_REQUEST, "Error al obtener el usuario", null).toJtoString();
			updated = SentenciasSQL.actualizarPerfil(nickname, us.getNombreCompleto(), us.getPais(),
					us.getFechaNacimiento(), us.getCorreo());
		} catch (NumberFormatException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.BAD_REQUEST, "Error: " + e.getMessage() + e.getStackTrace(), null)
					.toJtoString();
		}
		if (updated == 1) {
			us.setNickname(nickname);
			return new AppResponse(Status.OK, null, us.toString()).toJtoString();
		}
		if (updated == 0)
			return new AppResponse(Status.BAD_REQUEST,
					"No se ha actualizado la BBDD. Comprueba que el nickname esté dado de alta en la BBDD", null)
							.toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Error ", null).toJtoString();
	}
}
