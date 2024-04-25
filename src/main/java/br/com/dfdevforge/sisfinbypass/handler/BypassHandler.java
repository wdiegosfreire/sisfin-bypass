package br.com.dfdevforge.sisfinbypass.handler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class BypassHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private LambdaLogger logger = null;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		logger = context.getLogger();

		HttpResponse<String> httpResponse = null;
		APIGatewayProxyResponseEvent lambdaResponse = new APIGatewayProxyResponseEvent();

		try {
			this.log("event.getPath()", event.getPath());
			this.log("event.getResource()", event.getResource());

			if (event.getHttpMethod().equalsIgnoreCase("GET"))
				httpResponse = this.get(event);
			else
				httpResponse = this.post(event);

			// Preparando o response
			Map<String, String> headers = new HashMap<>();
			headers.put("Access-Control-Allow-Origin", "*");

			lambdaResponse.setBody(httpResponse.body());
			lambdaResponse.setHeaders(headers);
			
			lambdaResponse.setStatusCode(httpResponse.statusCode());
		}
		catch (Exception e) {
			this.log("Exception", "Entrou no bloco catch...");
			e.printStackTrace();
		}

		this.log("lambdaResponse", lambdaResponse);
		return lambdaResponse;
	}

	private HttpResponse<String> get(APIGatewayProxyRequestEvent event) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(this.getApplication(event.getPath())))
				.header("Content-Type", "application/json")
				.GET()
				.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

	private HttpResponse<String> post(APIGatewayProxyRequestEvent event) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();

		String token = "";
		if (event.getQueryStringParameters() != null && event.getQueryStringParameters().get("token") !=null)
			token = "?token=" + event.getQueryStringParameters().get("token");

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(this.getApplication(event.getPath()) + token))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(event.getBody()))
				.build();
		
		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public String getApplication(String resource) {
		String sisfinPath = "";

		if (resource.contains("maintenance")) {
			sisfinPath = "http://192.168.0.170:8080" + resource.split("maintenance")[1];
		}
		else {
			sisfinPath = "http://192.168.0.170:8081" + resource.split("transaction")[1];
		}

		return sisfinPath;
	}

	private void log(String identifier, Object value) {
		logger.log("SISFIN: " + identifier + ": " + value);
	}
}









