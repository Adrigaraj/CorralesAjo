package rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.json.JSONArray;

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

		String idPublicacion = pub.getIdPublicacion();
		String fechaPublicacion = new Date(new java.util.Date().getTime()).toString();
		pub.setFechaPublicacion(fechaPublicacion);
		String propietario = nickname;

		if (idPublicacion == null || propietario == null)
			return new AppResponse(Status.BAD_REQUEST, "Campos ifPublicacion o propietario vacíos", null).toJtoString();

		inserted = SentenciasSQL.insertPublicacion(idPublicacion, fechaPublicacion, propietario, pub.getTweet());

		if (inserted == 1)
			return new AppResponse(Status.CREATED, null, pub.toString()).toJtoString();
		if (inserted == 1062)
			return new AppResponse(Status.BAD_REQUEST, "Publicación ya introducida, cambie el idPublicacion", null)
					.toJtoString();
		if (inserted == 1452)
			return new AppResponse(Status.BAD_REQUEST,
					"El propietario de la publicación no está dado de alta en la BBDD", null).toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + inserted, null).toJtoString();
	}

	@DELETE
	@Path("{nickname}/{idPubli}")
	@Produces(MediaType.APPLICATION_JSON)
	public String borrarPublicacion(@PathParam("nickname") String nickname, @PathParam("idPubli") String idPubli) {
		log.debug("Petición recibida en borrarPublicacion(nickname, idPubli)");
		if (idPubli == null || nickname == null)
			return new AppResponse(Status.BAD_REQUEST, "Campos ifPublicacion o propietario vacíos", null).toJtoString();

		int deleted = SentenciasSQL.borrarPublicacion(idPubli, nickname);
		if (deleted == 1)
			return new AppResponse(Status.OK, null, "Objeto borrado correctamente").toJtoString();
		if (deleted == 0)
			return new AppResponse(Status.NO_CONTENT, "La publicación o propietario no se ha encontrado en la BBDD",
					null).toJtoString();
		else
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + deleted, null).toJtoString();
	}

	@GET
	@Path("{nickname}")
	@Produces(MediaType.APPLICATION_JSON)
	public String buscarPublicaciones(@PathParam("nickname") String nickname, @HeaderParam("fecha1") String fecha1,
			@HeaderParam("fecha1") String fecha2) {
		log.debug("Petición recibida en buscarPublicaciones(nickname)");
		JSONArray objDevolver = new JSONArray();
		ResultSet rs = null;
		if (nickname == null || nickname.equals("")) {
			return new AppResponse(Status.BAD_REQUEST, "No nick metido ", null).toJtoString();
		}
		try {
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(fecha1);
			Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(fecha2);

			rs = SentenciasSQL.selectPublicaciones(nickname);
			while (rs != null && rs.next()) {
				Publicacion pub = new Publicacion(rs.getString("idPublicacion"), rs.getString("fechaPublicacion"),
						rs.getString("propietario"), rs.getString("tweet"));
				Date fechPub = new SimpleDateFormat("dd/MM/yyyy").parse(pub.getFechaPublicacion());
				if (fechPub.after(date1) && fechPub.before(date2)) {
					objDevolver.put(pub.toJSON());
				}
			}
			return new AppResponseJSONValue(Status.OK, null, objDevolver).toJtoString();
		} catch (SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.BAD_REQUEST, "Código error: " + e.getErrorCode(), null).toJtoString();
		} catch (ParseException e) {
			log.error(e.getMessage() + e.getStackTrace());
			return new AppResponse(Status.BAD_REQUEST, "Formato fecha no aceptado", null).toJtoString();
		}
	}
}
