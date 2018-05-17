package rest;

import java.sql.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import bbdd.SentenciasSQL;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/publicaciones")
public class Publicaciones {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	@POST
	@Path("{nickname}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response añadirPublicacion(JAXBElement<Publicacion> publi, @PathParam("nickname") String nickname) {
		log.debug("Petición recibida en añadirPublicacion(publi, nickname)");
		Publicacion pub = publi.getValue();
		int inserted = 0;
		try {
			String idPublicacion = pub.getIdPublicacion();
			String fechaPublicacion = new Date(new java.util.Date().getTime()).toString();
			pub.setFechaPublicacion(fechaPublicacion);
			String propietario = nickname;

			if (idPublicacion == null || propietario == null)
				return Response.status(400).build();

			inserted = SentenciasSQL.insertPublicacion(idPublicacion, fechaPublicacion, propietario, pub.getTweet());
		} catch (NumberFormatException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return Response.status(400).build();
		}
		if (inserted == 1) {
			return Response.status(201).entity(pub).build();
		} else
			return Response.status(400).build();
	}

	@DELETE
	@Path("{nickname}/{idPubli}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrarPublicacion(@PathParam("nickname") String nickname, @PathParam("idPubli") String idPubli) {
		log.debug("Petición recibida en borrarPublicacion(nickname, idPubli)");
		if (idPubli == null || nickname == null)
			return Response.status(400).build();
		int deleted = SentenciasSQL.borrarPublicacion(idPubli, nickname);
		if (deleted == 1)
			return Response.status(200).build();
		else if (deleted == 0)
			return Response.status(204).build();
		else // if (deleted == -1)
			return Response.status(400).build();
	}

}
