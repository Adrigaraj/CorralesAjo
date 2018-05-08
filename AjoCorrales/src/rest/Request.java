package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("upmsocial")
public class Request {
	@Context
	private UriInfo uriInfo;

	private Connection conn;

	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	public Request() throws URISyntaxException {
		log.info("Dentro del constructor");
		Properties prop = new Properties();
		try {
			new FileInputStream("bbdd.properties");
			prop.load(getClass().getResourceAsStream("resources/bbdd.properties"));
			Class.forName(prop.getProperty("driver"));
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"),
					prop.getProperty("passwd"));
		} catch (ClassNotFoundException | SQLException | IOException e) {
			log.error(e.getMessage() + e.getStackTrace());
		}
	}

	// Lista de garajes JSON/XML generada con listas en JAXB
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsuarios() {
		log.info("Petición recibida en getUsuarios()");
		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM Usuarios";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				log.info(rs.getString("nickname"));
				log.info(rs.getString(2));
			}
			rs.close();
			return Response.status(Response.Status.OK).entity("OK").build(); // No se puede devolver el ArrayList (para
																				// generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error(e.getMessage() + e.getStackTrace());
				}
		}
	}

}
