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

import javassist.bytecode.stackmap.TypeData.ClassName;

import org.apache.log4j.Logger;


@Path("/upmsocial")
public class Request {
	@Context
	private UriInfo uriInfo;

	private DataSource ds;
	private Connection conn;
	
	private static final Logger log = Logger.getLogger(ClassName.class.getName());
	
	public Request() {
		log.info("Dentro del constructor");
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			NamingContext envCtx = (NamingContext) ctx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/UPMSocial");
			conn = ds.getConnection();
		} catch (NamingException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		}
	}

	// Lista de garajes JSON/XML generada con listas en JAXB
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getUsuarios(@QueryParam("offset") @DefaultValue("1") String offset,
			@QueryParam("count") @DefaultValue("10") String count) {
		PreparedStatement ps = null;
		Request g = new Request();
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
			return Response.status(Response.Status.OK).entity(g).build(); // No se puede devolver el ArrayList (para generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}finally {
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error(e.getMessage() + e.getStackTrace());
				}
		}
	}
	
	
}
