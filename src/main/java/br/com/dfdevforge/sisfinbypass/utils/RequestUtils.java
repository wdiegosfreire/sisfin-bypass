package br.com.dfdevforge.sisfinbypass.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RequestUtils {
	private JSONObject request;

	public RequestUtils(InputStream input) throws IOException, ParseException {
		this.request = getRequestJsonFromInputStream(input);
	}

	private JSONObject getRequestJsonFromInputStream(InputStream input) throws IOException, ParseException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		JSONParser parser = new JSONParser();

		return (JSONObject) parser.parse(reader);
	}

	public JSONObject getRequest() {
		return this.request;
	}

	public String getQueryParameter(String parameterName) {
		if (this.request.get("queryStringParameters") == null)
			return "";

		JSONObject queryParameterList = (JSONObject) this.request.get("queryStringParameters");
		return (String) queryParameterList.get(parameterName);
	}

	public String getPath() {
		return this.request.get("path") != null ? (String) this.request.get("path") : "";
	}

	public String getHttpMethod() {
		return this.request.get("httpMethod") != null ? (String) this.request.get("httpMethod") : "";
	}

	public boolean getIsBase64Encoded() {
		return this.request.get("isBase64Encoded") != null && (boolean) this.request.get("isBase64Encoded");
	}

	public String getBody() {
		return this.request.get("body") != null ? (String) this.request.get("body") : null;
	}

	public String getApplication() {
		String[] path = this.getPath().split("/");

		return path != null && path.length >= 2 ? path[1] : "";
	}
}