package hu.elte.txtuml.api.model.execution.diagnostics;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.GlobalSettings;
import hu.elte.txtuml.api.model.external.ModelClasses;

/**
 * Serves diagnostics data over HTTP.
 */
public class DiagnosticsServer {

	private HttpServer server;
	private ConcurrentMap<ModelClass, String> registry = new ConcurrentHashMap<>();

	public void start(Integer port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/" + GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PATH, new DiagnosticsHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public void stop() {
		if (server != null) server.stop(0);
		registry.clear();
	}

	public void register(ModelClass object, String currentLocation) {
		registry.put(object, currentLocation);
	}

	private class DiagnosticsHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			// Set required headers
			Headers headers = exchange.getResponseHeaders();
			headers.add("Content-type", "application/json");
			headers.add("Access-Control-Allow-Origin", "*");

			// Build the payload
			String response = registry.entrySet().stream()
					.map(DiagnosticsServer::registryEntryToJson)
					.collect(Collectors.joining(",", "[", "]"));

			// Write response
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

	}

	private static String registryEntryToJson(Entry<ModelClass, String> objectToLocation) {
		ModelClass object = objectToLocation.getKey();
		String location = objectToLocation.getValue();

		return "{\"class\":\"" + object.getClass().getCanonicalName() + "\","
				+ "\"id\":\"" + ModelClasses.getIdentifierOf(object) + "\","
				+ "\"location\":\"" + location + "\"}";
	}

}
