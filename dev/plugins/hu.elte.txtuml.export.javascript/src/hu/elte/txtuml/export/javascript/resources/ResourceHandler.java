package hu.elte.txtuml.export.javascript.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 
 * Handles tasks which involve resources
 *
 */
public class ResourceHandler {
	private static final String[] FILES = { "visualize.html", "visualizer.js", "lib/backbone-min.js", "lib/joint.css",
			"lib/joint.js", "lib/jquery.min.js", "lib/lodash.min.js", "visualizer/grid.js", "visualizer/linkholders.js",
			"visualizer/nodeholders.js", "visualizer/fonts.js", "visualizer/selector.js", "visualizer/shapes.js",
			"visualizer/utils.js", "visualizer/utils.js", "visualizer/attributeassociationview.js",
			"visualizer/visualizers.js" };

	/**
	 * Copies every file specified in the FILES list (containing the files which
	 * are needed for visualization) to the target directory keeping folder
	 * structure and overwriting existing files
	 * 
	 * @param targetDirectory
	 *            the absolute path to the directory to copy into
	 * @throws IOException
	 */
	public static void copyResourcesTo(String targetDirectory) throws IOException {
		Class<ResourceHandler> c = ResourceHandler.class;
		for (String file : FILES) {
			InputStream istream = c.getResourceAsStream("res/" + file);
			Path target = Paths.get(targetDirectory, file);

			if (!Files.exists(target)) {
				Files.createDirectories(target);
			}
			Files.copy(istream, Paths.get(targetDirectory, file), StandardCopyOption.REPLACE_EXISTING);
		}
	}

}
