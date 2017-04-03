package hu.elte.txtuml.export.javascript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.rmi.UnexpectedException;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import hu.elte.txtuml.export.javascript.json.JSONExporter;
import hu.elte.txtuml.export.javascript.json.model.ExportationModel;
import hu.elte.txtuml.export.javascript.json.model.cd.UnexpectedEndException;
import hu.elte.txtuml.export.javascript.resources.ResourceHandler;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.Pair;

/**
 * 
 * This class handles the JointJS visualization process
 *
 */
public class Exporter {

	private String target;
	private String genFolder;
	private TxtUMLLayoutDescriptor layout;
	private ExportationModel model;
	private String modelName;

	private static final String GENERATED_FILE = "input.js";

	private static final String GENERATED_FILE_HEAD = "var input = ";
	private static final String GENERATED_FILE_TAIL = ";";

	private static final String FILE_TO_OPEN_IN_BROWSER = "visualize.html";

	private static final String VISUALIZER_FOLDER = "js";

	/**
	 * Creates a new <code>Exporter</code> which will handle the exportation of
	 * the given diagrams.
	 * 
	 * @param layout
	 *            A descriptor which contains the layout descriptors, and
	 *            informations about the target project.
	 */
	public Exporter(TxtUMLLayoutDescriptor layout, String modelName) {
		this.layout = layout;
		this.modelName = modelName;
		String projectAbsLocation = ResourcesPlugin.getWorkspace().getRoot().getProject(layout.projectName)
				.getLocation().toFile().getAbsolutePath();
		genFolder = Paths.get(projectAbsLocation, layout.mappingFolder).toFile().getAbsolutePath();
		target = Paths.get(genFolder, VISUALIZER_FOLDER, this.modelName).toFile().getAbsolutePath();
		model = new ExportationModel();
		
	}

	/**
	 * Prepares the project for diagram visualization by copying the necessary
	 * JS files into it's folder.
	 * 
	 * @throws IOException
	 */
	private void prepare() throws IOException {
		ResourceHandler.copyResourcesTo(target);
	}

	/**
	 * Writes the JSON serialized model in a JavaScript wrapper to the target
	 * folder as input.js
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	private void insertInput() throws IOException, JAXBException {

		try (FileWriter fw = new FileWriter(Paths.get(target, GENERATED_FILE).toFile());
				BufferedWriter jsfile = new BufferedWriter(fw);) {
			jsfile.write(GENERATED_FILE_HEAD);
			JSONExporter.writeObjectAsJSON(model, jsfile);
			jsfile.write(GENERATED_FILE_TAIL);
		}
	}

	/**
	 * Opens visualize.html in the default browser
	 * 
	 * @throws PartInitException
	 * @throws MalformedURLException
	 */
	private void display() throws PartInitException, MalformedURLException {
		// set to use external browser due to internal browser bug
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=501978
		final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
		browser.openURL(Paths.get(target, FILE_TO_OPEN_IN_BROWSER).toUri().toURL());
	}

	/**
	 * Collects model and layout information of the diagrams the Exporter is
	 * initialized with, then generates the necessary files for JointJS
	 * visualization, finally it opens the visualize.html in the default browser
	 * which will visualize the diagrams
	 * 
	 * @throws ModelMapException
	 * @throws ArrangeException
	 * @throws UnknownDiagramTypeException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws PartInitException
	 * @throws UnexpectedEndException
	 */
	public void export() throws ModelMapException, ArrangeException, UnexpectedDiagramTypeException, IOException,
			JAXBException, PartInitException, UnexpectedEndException {
		createModel();
		prepare();
		insertInput();
		display();
	}

	/**
	 * Populates model with the diagrams to be exported
	 * 
	 * @throws ModelMapException
	 * @throws ArrangeException
	 * @throws UnknownDiagramTypeException
	 * @throws UnexpectedEndException
	 */
	private void createModel() throws ModelMapException, ArrangeException, UnexpectedDiagramTypeException,
			UnexpectedEndException, UnexpectedException {

		for (Pair<String, DiagramExportationReport> report : layout.getReportsWithDiagramNames()) {
			String name = report.getFirst();
			DiagramExportationReport der = report.getSecond();

			// map connects the layout information to the EMF-UML model
			// informations
			ModelMapProvider map = new ModelMapProvider(URI.createFileURI(genFolder), der.getModelName());

			model.createDiagram(name, report.getSecond(), map);

		}

	}

}
