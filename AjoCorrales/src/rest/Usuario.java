package rest;

import java.sql.Date;

import org.json.JSONObject;

public class Usuario {
	private String nickname;
	private String nombreCompleto;
	private int edad;
	private String pais;
	private Date fechaNacimiento;
	private String correo;
	private Date fechaAlta;

	public Usuario(String nickname, String nombreCompleto, int edad, String pais, Date fechaNacimiento, String correo,
			Date fechaAlta) {
		this.nickname = nickname;
		this.nombreCompleto = nombreCompleto;
		this.edad = edad;
		this.pais = pais;
		this.fechaNacimiento = fechaNacimiento;
		this.correo = correo;
		this.fechaAlta = fechaAlta;
	}

	public JSONObject toJSON() {
		JSONObject JsObj = new JSONObject();
		JsObj.put("nickname", this.nickname);
		JsObj.put("nombreCompleto", this.nombreCompleto);
		JsObj.put("edad", this.edad);
		JsObj.put("pais", this.pais);
		JsObj.put("fechaNacimiento", this.fechaNacimiento);
		JsObj.put("correo", this.correo);
		JsObj.put("fechaAlta", this.fechaAlta);
		return JsObj;
	}

	@Override
	public String toString() {
		return "Usuario [nickname=" + nickname + ", nombreCompleto=" + nombreCompleto + ", edad=" + edad + ", pais="
				+ pais + ", fechaNacimiento=" + fechaNacimiento + ", correo=" + correo + ", fechaAlta=" + fechaAlta
				+ "]";
	}

}
