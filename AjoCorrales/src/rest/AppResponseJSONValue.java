package rest;

import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

public class AppResponseJSONValue {
	Status status;
	String errorMessage;
	JSONArray data;

	public AppResponseJSONValue() {

	}

	public AppResponseJSONValue(Status status, String errorMessage, JSONArray data) {
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

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
