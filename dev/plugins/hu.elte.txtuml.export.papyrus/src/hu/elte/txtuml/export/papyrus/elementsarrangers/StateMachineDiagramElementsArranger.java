package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.TxtUmlPixelDimensionProvider;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsArranger implements IDiagramElementsArranger {

	private DiagramExportationReport report;
	private TxtUMLElementsMapper elementsMapper;
	private TxtUmlPixelDimensionProvider pixelDimensionProvider;

	public StateMachineDiagramElementsArranger(DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		//TODO: Separate pixelproviders for different diagram types
		this.pixelDimensionProvider = new TxtUmlPixelDimensionProvider(this.elementsMapper);
	}
	
	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getBoundsForElement(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Point> getRouteForConnection(Relationship connection) {
		// TODO Auto-generated method stub
		return null;
	}

}
