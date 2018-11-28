package hu.elte.txtuml.diagnostics.animation.js;

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

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.GlobalSettings;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.session.DiagnosticsPlugin;

/**
 * Serves diagnostics data over HTTP.
 */
public class DiagnosticsServer {

	private HttpServer server;
	private ConcurrentMap<String, RegistryEntry> registry = new ConcurrentHashMap<>();
	private DiagnosticsPlugin diagnosticsPlugin;
	
	public DiagnosticsServer(DiagnosticsPlugin diagnosticsPlugin){
		this.diagnosticsPlugin = diagnosticsPlugin;
	}

	public void start(Integer port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/" + GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PATH, new DiagnosticsHandler());
		server.createContext("/" + GlobalSettings.TXTUML_DIAGNOSTICS_DELAY_PATH, new DelayHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public void stop() {
		if (server != null) server.stop(0);
		registry.clear();
	}

	public void register(ModelEvent event) {
		RegistryEntry registryEntry = new RegistryEntry(event.modelClassName,
				event.modelClassInstanceName, event.eventTargetClassName);
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
			//Get request data
			StringBuilder body = new StringBuilder();
		    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody())) {
		        char[] buffer = new char[256];
		        int read;
		        while ((read = reader.read(buffer)) != -1) {
		            body.append(buffer, 0, read);
		        }
		    }
		    
            //Get and set animation delay
		    String[] delayArray = body.toString().split("=");
		    int delay = Integer.parseInt(delayArray[1]);
			setAnimationDelay(delay);
			
			// Write response
			exchange.sendResponseHeaders(200, 0);
			OutputStream os = exchange.getResponseBody();
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
		if (event.messageType == MessageType.PROCESSING_SIGNAL) {
			return;
		}
		
		this.register(event);
	}
	
	private void setAnimationDelay(int delay){
		int delayInMillisec = delay*1000;
		diagnosticsPlugin.setDelay(delayInMillisec);
	}

}