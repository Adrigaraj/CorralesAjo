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
		JSONObject a = new JSONObject();
		a.put("nickname", this.nickname);
		a.put("nombreCompleto", this.nombreCompleto);
		a.put("edad", this.edad);
		a.put("pais", this.pais);
		a.put("fechaNacimiento", this.fechaNacimiento);
		a.put("correo", this.correo);
		a.put("fechaAlta", this.fechaAlta);
		return a;
	}

	@Override
	public String toString() {
		return "Usuario [nickname=" + nickname + ", nombreCompleto=" + nombreCompleto + ", edad=" + edad + ", pais="
				+ pais + ", fechaNacimiento=" + fechaNacimiento + ", correo=" + correo + ", fechaAlta=" + fechaAlta
				+ "]";
	}

}
