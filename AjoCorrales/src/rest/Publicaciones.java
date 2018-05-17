package rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import bbdd.SentenciasSQL;
import javassist.bytecode.stackmap.TypeData.ClassName;

@Path("/publicaciones")
public class Publicaciones {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	@POST
	@Path("{nickname}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public String añadirPublicacion(JAXBElement<Publicacion> publi, @PathParam("nickname") String nickname) {
		log.debug("Petición recibida en añadirPublicacion(publi, nickname)");
		Publicacion pub = publi.getValue();
		int inserted = 0;
		AppResponse res;

		String idPublicacion = pub.getIdPublicacion();
		String fechaPublicacion = new Date(new java.util.Date().getTime()).toString();
		pub.setFechaPublicacion(fechaPublicacion);
		String propietario = nickname;

		if (idPublicacion == null || propietario == null)
			return new AppResponse(Status.BAD_REQUEST, "Campos ifPublicacion o propietario vacíos", "").toJtoString();

		inserted = SentenciasSQL.insertPublicacion(idPublicacion, fechaPublicacion, propietario, pub.getTweet());

		if (inserted == 1) {
			// return Response.status(201).entity(pub).build();
		} else {
		}
		// return Response.status(400).build();
		return "";
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

	@GET
	@Path("{nickname}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPublicaciones(@PathParam("nickname") String nickname, @HeaderParam("fecha1") String fecha1,
			@HeaderParam("fecha1") String fecha2) {
		log.debug("Petición recibida en buscarPublicaciones(nickname)");
		JSONObject objDevolver = new JSONObject();
		ResultSet rs = null;
		if (nickname == null || nickname.equals(""))
			objDevolver.put("error", "El campo nickname está vacío");
		else
			try {
				Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(fecha1);
				Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(fecha2);

				rs = SentenciasSQL.selectPublicaciones(nickname);
				int i = 1;
				while (rs.next()) {
					Publicacion pub = new Publicacion(rs.getString("idPublicacion"), rs.getString("propietario"),
							rs.getString("fechaPublicacion"), rs.getString("tweet"));
					Date fechPub = new SimpleDateFormat("dd-MM-yyyy").parse(pub.getFechaPublicacion());
					if (fechPub.after(date1) && fechPub.before(date2)) {
						objDevolver.put("Publicacion " + i, pub.toJSON());
						i++;
					}
				}
			} catch (SQLException e) {
				log.error(e.getMessage() + e.getStackTrace());
				objDevolver.put("error", "Error al ejecutar sentencia SQL");
			}
		return Response.status(400).build();
	}
}
