package rest;

import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

public class AppResponse {
	Status status;
	String errorMessage;
	String data;

	public AppResponse() {

	}

	public AppResponse(Status status, String errorMessage, String data) {
		this.status = status;
		this.errorMessage = errorMessage;
		this.data = data;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ErrorResponse [errorMessage=" + errorMessage + "]";
	}

	public JSONObject toJSON() {
		JSONObject JsObj = new JSONObject();
		JsObj.put("status", this.status);
		JsObj.put("errorMessage", this.errorMessage);
		JsObj.put("data", this.data);
		return JsObj;
	}

	public String toJtoString() {
		return toJSON().toString();
	}

}
