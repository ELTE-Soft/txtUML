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
	private static final String RESOURCE_FOLDER = "res";
	private static final String[] FILES = {
			// third party
			"lib/lodash.min.js", // Lodash library (required for jointJS)
			"lib/jquery.min.js", // jQuery library (required for jointJS)
			"lib/backbone-min.js", // backbone library (required for jointJS)
			"lib/joint.js", // jointJS library (used for visualization)
			"lib/joint.css", // jointJS default css file

			// visualizer files
			"visualize.html", // visualizer main layout file (to be loaded in
								// browser)

			"visualizer.js", // script to start the visualization and the input
								// processing

			"visualizer/grid.js", // defining grid class (used for abstract to
									// pixel grid conversions)

			"visualizer/linkholders.js", // containing wrappers for links
			"visualizer/nodeholders.js", // containing wrappers for nodes
			"visualizer/fonts.js", // containing helper methods for calculating
									// text bounding boxes as well as font size
									// informations

			"visualizer/selector.js", // responsible for navigation between
										// multiple diagrams

			"visualizer/shapes.js", // extended jointJS shapes to use for
									// visualization

			"visualizer/utils.js", // various utility methods, maps

			"visualizer/attributeassociationview.js", // a specialized jointJS
														// linkview for
														// displaying
														// associations with
														// names and
														// multiplicities

			"visualizer/visualizers.js" // holds visualizers for class and
										// statemachine diagrams
	};

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
			InputStream istream = c.getResourceAsStream(Paths.get(RESOURCE_FOLDER, file).toString());
			Path target = Paths.get(targetDirectory, file);

			if (!Files.exists(target)) {
				Files.createDirectories(target);
			}
			Files.copy(istream, Paths.get(targetDirectory, file), StandardCopyOption.REPLACE_EXISTING);
		}
	}

}
