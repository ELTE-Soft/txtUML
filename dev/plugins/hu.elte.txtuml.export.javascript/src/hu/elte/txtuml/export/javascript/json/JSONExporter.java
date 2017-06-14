package hu.elte.txtuml.export.javascript.json;

import java.io.BufferedWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.xmlmodel.ObjectFactory;
import org.eclipse.persistence.oxm.MediaType;

/**
 * 
 * Handles JSON serialization related tasks
 *
 */
public class JSONExporter {
	/**
	 * Serializes an Object to JSON and writes it using the writer provided
	 * 
	 * @param object
	 *            Object to write as JSON, which must have a no-arg constructor.
	 * @param jsonfile
	 *            BufferedWriter to write JSON Object into
	 * @throws JAXBException
	 */
	public static void writeObjectAsJSON(Object object, BufferedWriter jsonfile) throws JAXBException {
		JAXBContext jc = JAXBContextFactory.createContext(new Class[] { object.getClass(), ObjectFactory.class }, null);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
		marshaller.marshal(object, jsonfile);
	}
}
