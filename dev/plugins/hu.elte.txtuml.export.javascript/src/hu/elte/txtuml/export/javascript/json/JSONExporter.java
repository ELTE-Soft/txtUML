package hu.elte.txtuml.export.javascript.json;

import hu.elte.txtuml.export.javascript.json.model.ExportationModel;
import java.io.BufferedWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JSONExporter {
	public static void JSONFromReport(ExportationModel model, BufferedWriter jsfile) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(ExportationModel.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty("eclipselink.media-type", "application/json");
		marshaller.marshal(model, jsfile);
	}
}
