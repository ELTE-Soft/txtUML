package hu.elte.txtuml.export.cpp;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;

import org.eclipse.uml2.uml.Property;


public class Shared {
	public static List<Property> getProperties(Class class_) {
		List<Property> properties = new LinkedList<Property>();
		for (Association assoc : class_.getAssociations()) {
			for (Property prop : assoc.getMemberEnds()) {
				if (!prop.getType().equals(class_)) {
					properties.add(prop);
				}
			}
		}
		properties.addAll(class_.getOwnedAttributes());
		return properties;
	}


	@SuppressWarnings("unchecked")
	public static <ElementTypeT, EClassTypeT> void getTypedElements(Collection<ElementTypeT> dest_,
			Collection<Element> source_, EClassTypeT eClass_) {
		for (Element item : source_) {
			if (item.eClass().equals(eClass_)) {
				dest_.add((ElementTypeT) item);
			}
		}
	}

	// TODO need a better solution
	public static boolean isBasicType(String typeName_) {

		if (typeName_.equals("Integer") || typeName_.equals("Real") || typeName_.equals("Boolean")) {
			return true;
		} else {
			return false;
		}

	}

	public static void writeOutSource(String path_, String fileName_, String source_)
			throws FileNotFoundException, UnsupportedEncodingException {
		try {
			File file = new File(path_);
			if (!file.exists()) {
				file.mkdirs();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter writer = new PrintWriter(path_ + File.separator + fileName_, "UTF-8");
		writer.println(source_);
		writer.close();
	}

}
