package br.com.dfdevforge.sisfinbypass.handler;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

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
		

	    BypassHandler bypassHandler = new BypassHandler();

	    System.out.println(bypassHandler.getApplication("/transaction/location/accessModule"));
	    

	    assertNotNull(bypassHandler);
	}
}
