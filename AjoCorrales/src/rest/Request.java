package rest;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/upmsocial")
public class Request {
	@Context
	private UriInfo uriInfo;

	private Connection conn;

	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	public Request() throws URISyntaxException {
		log.info("Dentro del constructor");
		// Properties prop = new Properties();
		try {
			// new FileInputStream("bbdd.properties");
			// prop.load(getClass().getResourceAsStream("resources/bbdd.properties"));
			// Class.forName(prop.getProperty("driver"));
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/upmsocialdb?useSSL=false", "restuser1",
					"restuser1");
		} catch (ClassNotFoundException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		}
	}

	// Lista de garajes JSON/XML generada con listas en JAXB
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsuarios() {
		log.info("Petición recibida en getUsuarios()");
		PreparedStatement ps = null;
		JSONObject objDevolver = new JSONObject();
		try {
			String sql = "SELECT * FROM Usuarios";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				Usuario user = new Usuario(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getDate(5), rs.getString(6), rs.getDate(7));
				objDevolver.put("Usuario " + i, user.toJSON());
				i++;
			}
		} catch (NumberFormatException | SQLException e) {

		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error(e.getMessage() + e.getStackTrace());
				}
		}
		return objDevolver.toString();
	}

}
