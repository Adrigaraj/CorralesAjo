package rest;

import org.json.JSONObject;

public class Publicacion {
	private String idPublicacion;
	private String fechaPublicacion;
	private String propietario;
	private String tweet;

	public Publicacion() {

	}

	public Publicacion(String idPublicacion, String fechaPublicacion, String propietario, String tweet) {
		this.idPublicacion = idPublicacion;
		this.fechaPublicacion = fechaPublicacion;
		this.propietario = propietario;
		this.tweet = tweet;
	}

	public Publicacion(String idPublicacion, String propietario, String tweet) {
		this.idPublicacion = idPublicacion;
		this.fechaPublicacion = "";
		this.propietario = propietario;
		this.tweet = tweet;
	}

	public JSONObject toJSON() {
		JSONObject JsObj = new JSONObject();
		JsObj.put("idPublicacion", this.idPublicacion);
		JsObj.put("fechaPublicacion", this.fechaPublicacion);
		JsObj.put("propietario", this.propietario);
		JsObj.put("tweet", this.tweet);
		return JsObj;
	}

	public String getIdPublicacion() {
		return idPublicacion;
	}

	public void setIdPublicacion(String idPublicacion) {
		this.idPublicacion = idPublicacion;
	}

	public String getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(String fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	@Override
	public String toString() {
		return "Publicacion [idPublicacion=" + idPublicacion + ", fechaPublicacion=" + fechaPublicacion
				+ ", propietario=" + propietario + ", tweet=" + tweet + "]";
	}

}
