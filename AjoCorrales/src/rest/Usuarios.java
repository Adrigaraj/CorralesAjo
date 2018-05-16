package rest;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
		try {
			rs = SentenciasSQL.selectUsuarios();
			int i = 1;
			while (rs.next()) {
				Usuario user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"),
						rs.getString("pais"), rs.getString("fechaNacimiento"), rs.getString("correo"),
						rs.getDate("fechaAlta"));
				objDevolver.put("Usuario " + i, user.toJSON());
				i++;
			}
		} catch (NumberFormatException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		}
		return objDevolver.toString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarUsuario(JAXBElement<Usuario> user) {
		log.debug("Petición recibida en insertarUsuario()");
		int inserted = 0;
		Usuario us = user.getValue();
		try {
			String nickname = us.getNickname();
			String nombreCompleto = us.getNombreCompleto();
			String pais = us.getPais();
			String fechaNacimiento = us.getFechaNacimiento();
			String correo = us.getCorreo();
			Date fechaAlta = new Date(new java.util.Date().getTime());
			if (nickname == null)
				return Response.status(400).build();
			inserted = SentenciasSQL.insertUsuario(nickname, nombreCompleto, pais, fechaNacimiento, correo, fechaAlta);
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
