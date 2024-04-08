package br.com.dfdevforge.sisfinbypass.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.JsonObject;

public class BypassHandler implements RequestStreamHandler {

	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();

		logger.log("BEGIN");
		logger.log(input.toString());
		logger.log("END");

		JsonObject message = new JsonObject();
		message.addProperty("message", "Bolinha!!!!");

		JsonObject response = new JsonObject();
		response.add("body", message);

		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(response.toString());
		writer.close();
	}

	private void readInputStream(InputStream input) {
		
	}
}