package rest;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
		} catch (NumberFormatException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponseJSONValue(Status.OK, "Error al obtener los usuarios", null).toJtoString();
		}
		return new AppResponseJSONValue(Status.OK, "Error al obtener los usuarios", null).toJtoString();
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
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.OK, "Error al obtener los usuarios", null).toJtoString();
		}
		return new AppResponse(Status.OK, "Error al obtener el usuario", null).toJtoString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertarUsuario(JAXBElement<Usuario> user) {
		log.debug("Petición recibida en insertarUsuario(user)");
		int inserted = 0;
		Usuario us = user.getValue();
		try {
			String nickname = us.getNickname();
			String fechaAlta = new Date(new java.util.Date().getTime()).toString();
			us.setFechaAlta(fechaAlta);
			if (nickname == null)
				return new AppResponse(Status.OK, "Error al obtener el usuario", null).toJtoString();
			inserted = SentenciasSQL.insertUsuario(nickname, us.getNombreCompleto(), us.getPais(),
					us.getFechaNacimiento(), us.getCorreo(), fechaAlta);
		} catch (NumberFormatException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.OK, "Error al obtener el usuario", null).toJtoString();
		}
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

}
