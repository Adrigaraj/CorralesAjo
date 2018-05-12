package rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import bbdd.ConexionBBDD;
import bbdd.UtilsBBDD;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/Usuarios")
public class Usuarios {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());
	private Connection conn = ConexionBBDD.getConn();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsuarios() {
		log.info("Petición recibida en getUsuarios()");
		PreparedStatement ps = null;
		JSONObject objDevolver = new JSONObject();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM Usuarios";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				Usuario user = new Usuario(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getDate(5), rs.getString(6), rs.getDate(7));
				objDevolver.put("Usuario " + i, user.toJSON());
				i++;
			}
		} catch (NumberFormatException | SQLException e) {

		} finally {
			UtilsBBDD.cerrarRs(rs);
			UtilsBBDD.cerrarPs(ps);
		}
		return objDevolver.toString();
	}

}
