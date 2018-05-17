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
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import bbdd.SentenciasSQL;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/usuarios")
public class Usuarios {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsuarios() {
		log.debug("Petición recibida en getUsuarios()");
		JSONObject objDevolver = new JSONObject();
		ResultSet rs = null;
		Usuario user = null;
		try {
			rs = SentenciasSQL.selectUsuarios();
			int i = 1;
			if (rs != null)

				while (rs.next()) {
					user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"), rs.getString("pais"),
							rs.getString("fechaNacimiento"), rs.getString("correo"), rs.getString("fechaAlta"));
					objDevolver.put("Usuario " + i, user.toJSON());
					i++;
				}
			if (user != null)
				return objDevolver.toString();
		} catch (NumberFormatException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		}
		objDevolver.put("error", "Error al obtener los usuarios");
		return objDevolver.toString();
	}

	@GET
	@Path("{nickname}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verUsuario(@PathParam("nickname") String nickname) {
		log.debug("Petición recibida en getUsuario(nickname)");
		JSONObject objDevolver = new JSONObject();
		ResultSet rs = null;
		if (nickname == null || nickname.equals(""))
			objDevolver.put("error", "El campo nickname está vacío");
		else
			try {
				rs = SentenciasSQL.selectUsuario(nickname);
				if (rs != null && rs.next()) {
					Usuario user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"),
							rs.getString("pais"), rs.getString("fechaNacimiento"), rs.getString("correo"),
							rs.getString("fechaAlta"));
					objDevolver.put("Usuario", user.toJSON());
					return Response.status(201).entity(user).build();
				}
			} catch (SQLException e) {
				log.error(e.getMessage() + e.getStackTrace());
				objDevolver.put("error", "Error al ejecutar sentencia SQL");
			}
		return Response.status(400).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarUsuario(JAXBElement<Usuario> user) {
		log.debug("Petición recibida en insertarUsuario(user)");
		int inserted = 0;
		Usuario us = user.getValue();
		try {
			String nickname = us.getNickname();
			String fechaAlta = new Date(new java.util.Date().getTime()).toString();
			us.setFechaAlta(fechaAlta);
			if (nickname == null)
				return Response.status(400).build();
			inserted = SentenciasSQL.insertUsuario(nickname, us.getNombreCompleto(), us.getPais(),
					us.getFechaNacimiento(), us.getCorreo(), fechaAlta);
		} catch (NumberFormatException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return Response.status(400).build();
		}
		if (inserted == 1) {
			return Response.status(201).entity(us).build();
		} else
			return Response.status(400).build();
	}

}
