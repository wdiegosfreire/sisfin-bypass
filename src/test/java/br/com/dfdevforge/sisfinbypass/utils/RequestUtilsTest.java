package br.com.dfdevforge.sisfinbypass.utils;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserResourceTest")
class RequestUtilsTest {

	@Test
	void teste() throws IOException, ParseException {
		StringBuilder jsonMock = new StringBuilder();
		jsonMock.append("{ ");
		jsonMock.append("  \"path\": \"/transaction/user/executeAuthentication\", ");
		jsonMock.append("  \"httpMethod\": \"POST\", ");
		jsonMock.append("  \"isBase64Encoded\": true ");
		jsonMock.append("  \"body\": { ");
		jsonMock.append("      \"email\": \"wdiegosfreire@yahoo.com.br\", ");
		jsonMock.append("      \"password\": \"08afd6f9ae0c6017d105b4ce580de885\", ");
		jsonMock.append("  } ");
		jsonMock.append("}");

		InputStream input = new ByteArrayInputStream(jsonMock.toString().getBytes());

		RequestUtils requestUtils = new RequestUtils(input);

		this.log("getBody", requestUtils.getBody());
		this.log("getPath", requestUtils.getPath());
		this.log("getHttpMethod", requestUtils.getHttpMethod());
		this.log("getApplication", requestUtils.getApplication());
		this.log("getIsBase64Encoded", requestUtils.getIsBase64Encoded());

		assertNotNull(requestUtils);
	}

	private void log(String identifier, Object value) {
		System.out.println("SISFIN :: " + identifier + ": " + value);
	}
}