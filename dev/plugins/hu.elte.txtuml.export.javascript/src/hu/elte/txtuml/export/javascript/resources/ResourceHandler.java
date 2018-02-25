package hu.elte.txtuml.export.javascript.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.eclipse.emf.common.util.URI;

import hu.elte.txtuml.export.javascript.Activator;
import hu.elte.txtuml.utils.Logger;

/**
 * 
 * Handles tasks which involve resources
 *
 */
public class ResourceHandler {
	private static final String[] RESOURCE_FOLDER_PATH_SEGMENTS = new String[] { "src", "hu", "elte", "txtuml",
			"export", "javascript", "resources", "res" };
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

			"main.css", // visualizer main css style file

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

			"visualizer/visualizers.js", // holds visualizers for class and
										// statemachine diagrams
			
			"panAndZoom.js" //adds pan and zoom functionality to the diagram
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
		for (String file : FILES) {
			URL url;
			try {
				URI uri = URI.createPlatformPluginURI(Activator.PLUGIN_ID, true);
				uri = uri.appendSegments(RESOURCE_FOLDER_PATH_SEGMENTS);
				uri = uri.appendSegments(file.split("/"));
				url = new URL(uri.toString());
				InputStream istream = url.openConnection().getInputStream();

				Path target = Paths.get(targetDirectory, file);

				if (!Files.exists(target)) {
					Files.createDirectories(target);
				}
				
				Files.copy(istream, Paths.get(targetDirectory, file), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				Logger.sys.error("IOException during file copy", e);
			}
		}
	}

}
