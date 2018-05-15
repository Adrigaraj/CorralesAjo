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
			String sql = "SELECT 'nickname', 'nombreCompleto', 'edad', 'pais', 'fechaNacimiento', 'correo', 'fechaAlta' FROM Usuarios";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				Usuario user = new Usuario(rs.getString("nickname"), rs.getString("nombreCompleto"), rs.getInt("edad"),
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
	@Path("/{usuario}")
	@Consumes({ MediaType.APPLICATION_XML })
	public Response insertarUsuario(Usuario user) {
		log.debug("Petición recibida en insertarUsuario()");
		PreparedStatement ps = null;
		int inserted = 0;
		try {
			String nickname = user.getNickname();
			String nombreCompleto = user.getNombreCompleto();
			int edad = user.getEdad();
			String pais = user.getPais();
			Date fechaNacimiento = user.getFechaNacimiento();
			String correo = user.getCorreo();
			Date fechaAlta = user.getFechaAlta();
			if (nickname == null)
				return Response.status(400).build();
			String sql = "insert into Usuarios ('nickname', 'nombreCompleto', 'edad', 'pais', 'fechaNacimiento', 'correo', 'fechaAlta') "
					+ "values (" + nickname + ", " + nombreCompleto + ", " + edad + ", " + pais + ", " + fechaNacimiento
					+ ", " + correo + ", " + fechaAlta + ")";
			ps = conn.prepareStatement(sql);
			inserted = ps.executeUpdate();
		} catch (NumberFormatException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		} finally {
			UtilsBBDD.cerrarPs(ps);
		}
		if (inserted == 1)
			return Response.status(201).entity(user.getNickname().toString()).build();
		else
			return Response.status(400).build();
	}

}
