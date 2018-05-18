package hu.elte.txtuml.api.model.execution.diagnostics;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.GlobalSettings;

/**
 * Serves diagnostics data over HTTP during execution from the {@link DiagnosticsRegistry} passed to the constructor.
 * The resource path and port are defined in {@link GlobalSettings}.
 * 
 * The set port currently introduces a limitation: if there is already an execution in progress, or the port is
 * otherwise occupied, the server will not be set up.
 * 
 * @author szokolai-mate
 *
 */

@SuppressWarnings("restriction")
public class DiagnosticsServer implements HttpHandler{
	
	private HttpServer server;
	private DiagnosticsRegistry registry;

	public DiagnosticsServer(DiagnosticsRegistry registry) {
		this.registry = registry;
	}
	
	public void start() throws IOException{
		this.server = HttpServer.create(new InetSocketAddress(GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PORT), 0);
        server.createContext("/"+GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PATH, this);
        server.setExecutor(null); // creates a default executor
        server.start();
	}
	
	@Override
    public void handle(HttpExchange t) throws IOException {
		//Set required headers
		Headers headers = t.getResponseHeaders();
		headers.add("Content-type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		
		//Build the payload
        String response = "[";
        Boolean first = true;
        for( DiagnosticsRegistryEntry e : this.registry.getRegistry()){
        	if(!first) response+=",";
        	first = false;
        	response += e.toJSON();
        }
        response+= "]";
        
        //Write response
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
