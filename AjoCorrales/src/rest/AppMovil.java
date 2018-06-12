package rest;

import org.json.JSONObject;

public class AppMovil {

	String nickname;
	String nombreCompleto;
	String pais;
	String fechaNacimiento;
	String correo;
	String fechaAlta;
	String idPublicacion;
	String fechaPublicacion;
	String propietario;
	String ultimoEstado;
	int numAmigos;
	String tweet1;
	String tweet2;
	String tweet3;
	String tweet4;
	String tweet5;
	String tweet6;
	String tweet7;
	String tweet8;
	String tweet9;
	String tweet10;

	public AppMovil(String nickname, String nombreCompleto, String pais, String fechaNacimiento, String correo,
			String fechaAlta, String idPublicacion, String fechaPublicacion, String propietario, String ultimoEstado,
			int numAmigos, String tweet1, String tweet2, String tweet3, String tweet4, String tweet5, String tweet6,
			String tweet7, String tweet8, String tweet9, String tweet10) {
		this.nickname = nickname;
		this.nombreCompleto = nombreCompleto;
		this.pais = pais;
		this.fechaNacimiento = fechaNacimiento;
		this.correo = correo;
		this.fechaAlta = fechaAlta;
		this.idPublicacion = idPublicacion;
		this.fechaPublicacion = fechaPublicacion;
		this.propietario = propietario;
		this.ultimoEstado = ultimoEstado;
		this.numAmigos = numAmigos;
		this.tweet1 = tweet1;
		this.tweet2 = tweet2;
		this.tweet3 = tweet3;
		this.tweet4 = tweet4;
		this.tweet5 = tweet5;
		this.tweet6 = tweet6;
		this.tweet7 = tweet7;
		this.tweet8 = tweet8;
		this.tweet9 = tweet9;
		this.tweet10 = tweet10;
	}

	public JSONObject toJSON() {
		JSONObject JsObj = new JSONObject();
		JsObj.put("nickname", this.nickname);
		JsObj.put("nombreCompleto", this.nombreCompleto);
		JsObj.put("pais", this.pais);
		JsObj.put("fechaNacimiento", this.fechaNacimiento);
		JsObj.put("correo", this.correo);
		JsObj.put("fechaAlta", this.fechaAlta);
		return JsObj;
	}

}
