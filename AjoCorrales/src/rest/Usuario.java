package rest;

import java.sql.Date;

import org.json.JSONObject;

public class Usuario {
	private String nickname;
	private String nombreCompleto;
	private String pais;
	private String fechaNacimiento;
	private String correo;
	private Date fechaAlta;

	public Usuario() {
	}

	public Usuario(String nickname, String nombreCompleto) {
		this.nickname = nickname;
		this.nombreCompleto = nombreCompleto;
	}

	public Usuario(String nickname, String nombreCompleto, String pais, String fechaNacimiento, String correo,
			Date fechaAlta) {
		this.nickname = nickname;
		this.nombreCompleto = nombreCompleto;
		this.pais = pais;
		this.fechaNacimiento = fechaNacimiento;
		this.correo = correo;
		this.fechaAlta = fechaAlta;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Override
	public String toString() {
		return "Usuario [nickname=" + nickname + ", nombreCompleto=" + nombreCompleto + ", pais=" + pais
				+ ", fechaNacimiento=" + fechaNacimiento + ", correo=" + correo + ", fechaAlta=" + fechaAlta + "]";
	}

}
