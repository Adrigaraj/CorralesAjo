package rest;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
		Properties prop = new Properties();
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

	@GET
	@Path("saluda/{nombre}/{apellido}")
	@Produces(MediaType.TEXT_HTML)
	public String saludoHtml(@PathParam("nombre") String n, @PathParam("apellido") String a,
			@QueryParam("apellido2") String a2) {
		return "<html>" + "<title>" + "Hola JAX-RS" + "</title>" + "<body><h1>" + "Hola " + n + " " + a + " " + a2
				+ "</body></h1>" + "</html> ";
	}

	// Lista de garajes JSON/XML generada con listas en JAXB
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsuarios() {
		log.info("Petición recibida en getUsuarios()");
		PreparedStatement ps = null;
		JSONObject asd = new JSONObject();
		try {
			String sql = "SELECT * FROM Usuarios";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				Usuario user = new Usuario(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getDate(5), rs.getString(6), rs.getDate(7));
				asd.put("Usuario " + i, user.toJSON());
				i++;
			}
			rs.close();

		} catch (NumberFormatException | SQLException e) {

		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error(e.getMessage() + e.getStackTrace());
				}
		}
		return asd.toString();
	}

}
