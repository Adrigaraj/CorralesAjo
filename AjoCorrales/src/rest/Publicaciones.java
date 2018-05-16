package rest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import bbdd.ConexionBBDD;
import bbdd.UtilsBBDD;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/publicaciones")
public class Publicaciones {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());
	private Connection conn = ConexionBBDD.getConn();

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response añadirPublicacion(JAXBElement<Publicacion> publi) {
		Publicacion pub = publi.getValue();
		PreparedStatement ps = null;
		int inserted = 0;
		String sql;
		try {
			try {
				String idPublicacion = pub.getIdPublicacion();
				Date fechaPublicacion = new Date(System.currentTimeMillis());
				String propietario = pub.getPropietario();
				String tweet = pub.getTweet();

				if (idPublicacion == null || propietario == null)
					return Response.status(400).build();

				sql = "insert into Publicaciones (idPublicacion, fechaPublicacion, propietario, tweet) " + "values ('"
						+ idPublicacion + "', '" + fechaPublicacion + "', '" + propietario + "', '" + tweet + "')";
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
			return Response.status(201).entity(pub).build();
		} else
			return Response.status(400).build();
	}
	//
	// @DELETE
	// @Path("/{publicacion}")
	// @Consumes(MediaType.APPLICATION_JSON)
	// public Response borrarPublicacion(@PathParam("publicacion") String
	// idPublicacion) {
	// if()
	// }

}
