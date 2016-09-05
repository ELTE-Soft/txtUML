package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.cdt.core.ToolFactory;
import org.eclipse.cdt.core.formatter.CodeFormatter;



import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;

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

		return typeName_.equals("Integer") || typeName_.equals("Real") || typeName_.equals("Boolean");

	}

	public static boolean generatedClass(Class item) {
		return item.getName().startsWith("#");
	}

	public static List<Parameter> getSignalConstructorParameters(Signal signal, EList<Element> elements) {
		List<Parameter> signalParameters = new LinkedList<Parameter>();

		Class factoryClass = getSignalFactoryClass(signal, elements);
		if (factoryClass != null) {
			for (Operation op : factoryClass.getOperations()) {
				if (isConstructor(op)) {

					signalParameters.addAll(op.getOwnedParameters());
				}
			}
		}

		// TODO need better solution
		signalParameters.removeIf(s -> s.getType().getName().equals(signal.getName()));

		return signalParameters;
	}

	public static String signalCtrBody(Signal signal, EList<Element> elements) {
		ActivityExporter activityExporter = new ActivityExporter();
		Class factoryClass = getSignalFactoryClass(signal, elements);
		String body = "";
		for (Operation operation : factoryClass.getOperations()) {
			if (isConstructor(operation)) {
				body = activityExporter.createfunctionBody(Shared.getOperationActivity(operation)).toString();

			}
		}

		return body;

	}

	public static Activity getOperationActivity(Operation operation) {
		Activity activity = null;
		for (Behavior behavior : operation.getMethods()) {

			if (behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
				activity = (Activity) behavior;
			} else {
				// TODO exception, unknown for me, need the model
			}
		}

		return activity;
	}

	public static Class getSignalFactoryClass(Signal signal, EList<Element> elements) {
		for (Element element : elements) {
			if (element.eClass().equals(UMLPackage.Literals.CLASS)) {
				Class cls = (Class) element;
				for (Operation operation : cls.getOperations()) {
					if (isConstructor(operation)) {
						for (Parameter param : operation.getOwnedParameters()) {
							if (param.getType().getName().equals(signal.getName()))
								return cls;
						}
					}

				}
			}
		}

		return null;
	}

	public static boolean isConstructor(Operation operation) {

		for (Stereotype stereotype : operation.getAppliedStereotypes()) {

			if (stereotype.getKeyword().equals(ActivityTemplates.CreateStereoType)) {
				return true;

			}
		}

		return false;

	}

	public static void writeOutSource(String path_, String fileName_, String source)
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
		writer.println(source);
		writer.close();
	}

	public static String format(String source) {
		
		CodeFormatter formatter = ToolFactory.createDefaultCodeFormatter(null);
		TextEdit edit = formatter.format(CodeFormatter.K_TRANSLATION_UNIT, source, 0, source.length(), 0, null);		
		IDocument document = new Document(source);
		try {
			edit.apply(document);
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String formattedSource = document.get();
		
		return formattedSource;
	}

}
