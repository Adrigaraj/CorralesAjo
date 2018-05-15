package rest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

import bbdd.ConexionBBDD;
import bbdd.UtilsBBDD;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/upmsocial/usuarios")
public class Usuarios {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());
	private Connection conn = ConexionBBDD.getConn();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsuarios() {
		log.debug("Petición recibida en getUsuarios()");
		PreparedStatement ps = null;
		JSONObject objDevolver = new JSONObject();
		ResultSet rs = null;
		try {
			String sql = "SELECT nickname, nombreCompleto, pais, fechaNacimiento, correo, fechaAlta FROM Usuarios";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				Usuario user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"),
						rs.getString("pais"), rs.getDate("fechaNacimiento"), rs.getString("correo"),
						rs.getDate("fechaAlta"));
				objDevolver.put("Usuario " + i, user.toJSON());
				i++;
			}
		} catch (NumberFormatException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		} finally {
			UtilsBBDD.cerrarRs(rs);
			UtilsBBDD.cerrarPs(ps);
		}
		return objDevolver.toString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarUsuario(JAXBElement<Usuario> user) {
		log.debug("Petición recibida en insertarUsuario()");
		PreparedStatement ps = null;
		int inserted = 0;
		Usuario us = user.getValue();
		String sql;
		try {
			try {
				String nombreCompleto = us.getNombreCompleto();
				String nickname = us.getNickname();
				String pais = us.getPais();
				Date fechaNacimiento = us.getFechaNacimiento();
				String correo = us.getCorreo();
				Date fechaAlta = new Date(System.currentTimeMillis());
				if (nickname == null)
					return Response.status(400).build();
				if (fechaNacimiento != null)
					sql = "insert into Usuarios (nickname, nombreCompleto,  pais, fechaNacimiento, correo, fechaAlta) "
							+ "values ('" + nickname + "', '" + nombreCompleto + "', '" + pais + "', '"
							+ fechaNacimiento + "', '" + correo + "', '" + fechaAlta + "')";
				else
					sql = "insert into Usuarios (nickname, nombreCompleto,  pais, correo, fechaAlta) " + "values ('"
							+ nickname + "', '" + nombreCompleto + "', '" + pais + "', '" + correo + "', '" + fechaAlta
							+ "')";
				ps = conn.prepareStatement(sql);
				inserted = ps.executeUpdate();
			} catch (NumberFormatException e) {
				log.error(e.getMessage() + e.getStackTrace());
				return Response.status(400).build();
			} catch (SQLException seRs) {
				String exMsg = "Message from MySQL Database";
				String exSqlState = "Exception";
				SQLException mySqlEx = new SQLException(exMsg, exSqlState);
				seRs.setNextException(mySqlEx);
				throw seRs;
			} finally {
				UtilsBBDD.cerrarPs(ps);
			}
		} catch (SQLException se) {
			System.out.println("Code: " + se.getErrorCode());
			System.out.println("SqlState: " + se.getSQLState());
			System.out.println("Error Message: " + se.getMessage());
			se = se.getNextException();
			return Response.status(400).entity("Doble PK").build();
		}
		if (inserted == 1) {
			return Response.status(201).entity(us).build();
		} else
			return Response.status(400).build();
	}

}
