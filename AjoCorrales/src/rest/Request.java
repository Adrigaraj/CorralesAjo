package rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.naming.NamingContext;

@Path("/upmsocial")
public class Request {
	@Context
	private UriInfo uriInfo;

	private DataSource ds;
	private Connection conn;

	public Request() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			NamingContext envCtx = (NamingContext) ctx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/GarajesyEmpleados");
			conn = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Lista de garajes JSON/XML generada con listas en JAXB
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getGarajes2(@QueryParam("offset") @DefaultValue("1") String offset,
			@QueryParam("count") @DefaultValue("10") String count) {
		try {
			int off = Integer.parseInt(offset);
			int c = Integer.parseInt(count);
			String sql = "SELECT * FROM Garaje order by id LIMIT " + (off - 1) + "," + c + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			Request g = new Request();
			rs.beforeFirst();
			while (rs.next()) {
				
			}
			return Response.status(Response.Status.OK).entity(g).build(); // No se puede devolver el ArrayList (para generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	
	
}
