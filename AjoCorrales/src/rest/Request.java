package rest;

import java.net.URISyntaxException;
import java.sql.Connection;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import bbdd.ConexionBBDD;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/upmsocial")
public class Request {
	@Context
	private UriInfo uriInfo;

	private Connection conn;

	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	public Request() throws URISyntaxException {
		log.info("Dentro del constructor");
		conn = ConexionBBDD.getConn();
	}

}
