package br.com.dfdevforge.sisfinbypass.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import br.com.dfdevforge.sisfinbypass.utils.RequestUtils;

public class BypassHandler implements RequestHandler<InputStream, JSONObject> {

	public JSONObject handleRequest(InputStream input, Context context) {
		HttpResponse<String> httpResponse = null;
		JSONObject lambdaResponse = new JSONObject();

		try {
			RequestUtils requestUtils = new RequestUtils(input);

			this.log("getBody", requestUtils.getBody());
			this.log("getRequest", requestUtils.getRequest());
			this.log("getQueryParameter", requestUtils.getQueryParameter("token"));
			this.log("getCompleteUrl", "http://192.168.0.170:8080/user/executeAuthentication");

//			httpResponse = this.post(null, requestUtils.getBody());
//			lambdaResponse = this.prepareLambdaResponse(httpResponse, requestUtils);

			lambdaResponse = this.prepareLambdaMock();
		}
		catch (Exception e) {
			this.log("Exception", "Entrou no bloco catch...");
			e.printStackTrace();
		}

		this.log("lambdaResponse", lambdaResponse);
		return lambdaResponse;
	}

	private HttpResponse<String> post(String url, String body) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://192.168.0.170:8080/user/executeAuthentication"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

	private JSONObject prepareLambdaResponse(HttpResponse<String> httpResponse, RequestUtils requestUtils) {
		JSONObject lambdaResponse = new JSONObject();
		Gson gson = new Gson(); 

		lambdaResponse.put("headers", null);
		lambdaResponse.put("multiValueHeaders", null);

		lambdaResponse.put("body", gson.fromJson(httpResponse.body(), JSONObject.class));
		lambdaResponse.put("statusCode", httpResponse.statusCode());
		lambdaResponse.put("isBase64Encoded", requestUtils.getIsBase64Encoded());

		return lambdaResponse;
	}

	private JSONObject prepareLambdaMock() {
		Map<String, Object> lambdaResponse = new HashMap<>();

//		lambdaResponse.put("headers", null)
//		lambdaResponse.put("multiValueHeaders", null)

		lambdaResponse.put("statusCode", 200);
		lambdaResponse.put("body", this.getMock());
		lambdaResponse.put("isBase64Encoded", false);

		return new JSONObject(lambdaResponse);
	}

	private void log(String identifier, Object value) {
		System.out.println("SISFIN: " + identifier + ":\n" + value);
	}

	private String getMock() {
		StringBuilder mock = new StringBuilder();

		mock.append("{ ");
		mock.append("    \"map\": { ");
		mock.append("        \"token\": \"ZXlKMGVYQWlPaUpLVjFRaUxDSmhiR2NpT2lKSVV6VXhNaUo5LmV5SnpkV0lpT2lKM1pHbGxaMjl6Wm5KbGFYSmxRSGxoYUc5dkxtTnZiUzVpY2lJc0luVnpaWEpKWkdWdWRHbDBlU0k2TVN3aVpYaHdJam94TnpFek1ETTJNak0zZlEuV05CWHZtRVZMeHpNVXFqSUFjaGNZQzlveTAyb1dVWGQ0OU9xWjY3MlphQUNndjNIRzdEWEFkSmpXMTVVWllUTGFzeTlTZDMxRnBmbkJKVHpkNGJqVGc=\", ");
		mock.append("        \"userAuthenticated\": { ");
		mock.append("            \"map\": null, ");
		mock.append("            \"identity\": 1, ");
		mock.append("            \"name\": \"diego freire\", ");
		mock.append("            \"password\": \"9dca979c0d02e1f92c7f1b8e324e5340\", ");
		mock.append("            \"email\": \"wdiegosfreire@yahoo.com.br\" ");
		mock.append("        } ");
		mock.append("    }, ");
		mock.append("    \"token\": \"ZXlKMGVYQWlPaUpLVjFRaUxDSmhiR2NpT2lKSVV6VXhNaUo5LmV5SnpkV0lpT2lKM1pHbGxaMjl6Wm5KbGFYSmxRSGxoYUc5dkxtTnZiUzVpY2lJc0luVnpaWEpKWkdWdWRHbDBlU0k2TVN3aVpYaHdJam94TnpFek1ETTJNak0zZlEuV05CWHZtRVZMeHpNVXFqSUFjaGNZQzlveTAyb1dVWGQ0OU9xWjY3MlphQUNndjNIRzdEWEFkSmpXMTVVWllUTGFzeTlTZDMxRnBmbkJKVHpkNGJqVGc=\" ");
		mock.append("} ");

		return mock.toString();
	}
}









