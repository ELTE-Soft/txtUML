package hu.elte.txtuml.export.javascript;

import hu.elte.txtuml.export.javascript.json.JSONExportModel;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.algorithms.FileVisualize;
import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxOverlapConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementsConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class JSONExporter {
	public static String JSONFromReport(Collection<DiagramExportationReport> collection) throws ArrangeException, JAXBException {
		for (DiagramExportationReport der : collection){
			LayoutVisualizerManager lvm = new LayoutVisualizerManager(der.getNodes(), der.getLinks(), der.getStatements());
			lvm.arrange();
			
			FileVisualize.printOutput(lvm.getObjects(), lvm.getAssociations(), "D:\\test.txt");
			
			JSONExportModel test = new JSONExportModel(lvm.getObjects(), lvm.getAssociations());
	        JAXBContext jc = JAXBContext.newInstance(JSONExportModel.class);
	        Marshaller marshaller = jc.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty("eclipselink.media-type", "application/json");
	        marshaller.marshal(test, System.out);
		}
		return "ok";
	}
}
