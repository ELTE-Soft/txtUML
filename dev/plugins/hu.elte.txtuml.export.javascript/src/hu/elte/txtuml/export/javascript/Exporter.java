package hu.elte.txtuml.export.javascript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import hu.elte.txtuml.export.javascript.json.JSONExporter;
import hu.elte.txtuml.export.javascript.json.model.ExportationModel;
import hu.elte.txtuml.export.javascript.resources.ResourceHandler;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.Pair;

public class Exporter {
	
	private String target;
	private String genFolder;
	private TxtUMLLayoutDescriptor layout;
	private ExportationModel model;
	
	public Exporter(TxtUMLLayoutDescriptor layout){
		this.layout = layout;
		String projectAbsLocation = ResourcesPlugin.getWorkspace().getRoot().getProject(layout.projectName).getLocation().toFile()
				.getAbsolutePath();
		genFolder = Paths.get(projectAbsLocation,layout.mappingFolder).toFile().getAbsolutePath();
		target = Paths.get(genFolder,"js").toFile().getAbsolutePath();
		model = new ExportationModel();
	}
	
	private void prepare() throws IOException{
		ResourceHandler.copyResourcesTo(target);
	}
	
	private void insertInput() throws IOException, JAXBException{	
				
		try(
			FileWriter fw = new FileWriter(Paths.get(target, "input.js").toFile());
			BufferedWriter jsfile = new BufferedWriter(fw);
		){
			jsfile.write("var input = ");
			JSONExporter.JSONFromReport(model, jsfile);
			jsfile.write(";");
			
		} catch (IOException e) {
			throw e;
		} catch (JAXBException e) {
			throw e;
		}
	}
	
	private void display() throws PartInitException, MalformedURLException{
		final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser("hu.elte.txtuml.export.javascript");
		browser.openURL(Paths.get(target, "visualize.html").toUri().toURL());
	}
	
	public void export() throws Exception{
		createModel();
		prepare();
		insertInput();
		display();
	}

	private void createModel() throws ModelMapException, ArrangeException {
		
		for (Pair<String,DiagramExportationReport> report : layout.getReportsWithDiagramNames()){
				String name = report.getFirst();
				DiagramExportationReport der = report.getSecond();
				
				LayoutVisualizerManager lvm = new LayoutVisualizerManager(der.getNodes(), der.getLinks(), der.getStatements());
				lvm.arrange();
				
				ModelMapProvider map = new ModelMapProvider(URI.createFileURI(genFolder), der.getModelName());
				switch (der.getType()){
					case Class: model.addClassDiagram(name, lvm.getObjects(), lvm.getAssociations(), map); break;
					case StateMachine: model.addStateChartDiagram(name, lvm.getObjects(), lvm.getAssociations(), map); break;
				}
		}
		
	}


}
