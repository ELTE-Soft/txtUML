package hu.elte.txtuml.diagnostics.animation.js;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.session.DiagnosticsPlugin;

/**
 * Serves diagnostics data over HTTP.
 */
public class DiagnosticsServer {

	public static final String TXTUML_DIAGNOSTICS_HTTP_PATH = "registry";
	public static final String TXTUML_DIAGNOSTICS_DELAY_PATH = "delay";

	private HttpServer server;
	private ConcurrentMap<String, RegistryEntry> registry = new ConcurrentHashMap<>();
	private DiagnosticsPlugin diagnosticsPlugin;

	public DiagnosticsServer(DiagnosticsPlugin diagnosticsPlugin) {
		this.diagnosticsPlugin = diagnosticsPlugin;
	}

	public void start(Integer port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/" + TXTUML_DIAGNOSTICS_HTTP_PATH, new DiagnosticsHandler());
		server.createContext("/" + TXTUML_DIAGNOSTICS_DELAY_PATH, new DelayHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public void stop() {
		if (server != null) server.stop(0);
		registry.clear();
	}

	public void register(ModelEvent event) {
		RegistryEntry registryEntry = new RegistryEntry(event.modelClassName, event.modelClassInstanceName,
			event.eventTargetClassName);
		registry.put(event.modelClassInstanceID, registryEntry);
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

	private class DelayHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			// Set required headers
			Headers headers = exchange.getResponseHeaders();
			headers.add("Content-type", "application/json");
			headers.add("Access-Control-Allow-Origin", "*");

			String body = "";
			String response = "";

			if ("POST".equals(exchange.getRequestMethod())) {
				// Get request data
				try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody())) {
					body = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
				}

				// Get and set animation delay
				String[] animationDelayArray = body.split("=");
				int animationDelay = Integer.parseInt(animationDelayArray[1]);
				setAnimationDelay(animationDelay);
			} else {
				// Build the payload
				response = "{\"animationDelay\":\"" + diagnosticsPlugin.getAnimationDelay() + "\"}";
			}

			// Write response
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

	}

	private static String registryEntryToJson(Entry<String, RegistryEntry> instanceIdToEntry) {
		String instanceId = instanceIdToEntry.getKey();
		RegistryEntry entry = instanceIdToEntry.getValue();

		return "{\"class\":\"" + entry.getModelClassName() + "\","
			+ "\"id\":\"" + instanceId + "\","
			+ "\"name\":\"" + entry.getModelClassInstanceName() + "\","
			+ "\"location\":\"" + entry.getLocationName() + "\"}";
	}

	public void animateEvent(ModelEvent event) {
		if (event.messageType != MessageType.PROCESSING_SIGNAL) {
			register(event);
		}
	}

	private void setAnimationDelay(int animationDelay) {
		diagnosticsPlugin.setAnimationDelay(animationDelay);
	}

}
