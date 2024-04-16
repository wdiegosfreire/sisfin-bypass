package br.com.dfdevforge.sisfinbypass.handler;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserResourceTest")
class BypassHandlerTest {

	@Test 
	void testLoadEventBridgeEvent() throws IOException {
		StringBuilder jsonMock = new StringBuilder();
		jsonMock.append("{");
		jsonMock.append("  \"path\": \"/maintenance/user/executeAuthentication\"");
		jsonMock.append("}");
		
		InputStream input = new ByteArrayInputStream(jsonMock.toString().getBytes());

	    BypassHandler bypassHandler = new BypassHandler();
	    bypassHandler.handleRequest(input, null);

	    assertNotNull(bypassHandler);
	}
}
