package hu.elte.txtuml.export.javascript.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceHandler {
	private static final String[] FILES = {
			"visualize.html",
			"visualizer.js",
			"visualizer.css",
			"lib/backbone-min.js",
			"lib/joint.css",
			"lib/joint.js",
			"lib/jquery.min.js",
			"lib/lodash.min.js",
			"visualizer/grid.js",
			"visualizer/linkholders.js",
			"visualizer/nodeholders.js",
			"visualizer/selector.js",
			"visualizer/shapes.js",
			"visualizer/utils.js",
			"visualizer/utils.js",
			"visualizer/attributeassociationview.js"
			};

	public static void copyResourcesTo(String targetDirectory) throws IOException{
			Class<ResourceHandler> c = ResourceHandler.class;
			for (String file : FILES){
				InputStream istream = c.getResourceAsStream("res/" + file);
				Path target = Paths.get(targetDirectory, file);
				if (!Files.exists(target)){
					Files.createDirectories(target);
				}
				Files.copy(istream, Paths.get(targetDirectory, file), StandardCopyOption.REPLACE_EXISTING);
			}
	}
	
	
}
