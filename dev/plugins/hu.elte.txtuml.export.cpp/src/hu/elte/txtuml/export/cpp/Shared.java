package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.activity.ActivityExporter;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.cdt.core.ToolFactory;
import org.eclipse.cdt.core.formatter.CodeFormatter;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.utils.Pair;

public class Shared {
	@SuppressWarnings("unchecked")
	public static <ElementTypeT, EClassTypeT> void getTypedElements(Collection<ElementTypeT> dest,
			Collection<Element> source, EClassTypeT eClass) {
		for (Element item : source) {
			if (item.eClass().equals(eClass)) {
				dest.add((ElementTypeT) item);
			}
		}
	}

	// TODO need a better solution
	public static boolean isBasicType(String typeName) {

		return typeName.equals("Integer") || 
			   typeName.equals("Real") || 
			   typeName.equals("Boolean");

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
					for (Parameter parameter : op.getOwnedParameters()) {
						if (!parameter.getType().getName().equals(signal.getName())) {
							signalParameters.add(parameter);
						}
					}
					break;
				}
			}
		}

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

	public static List<String> getOperationParamTypes(Operation operation) {
		List<String> operationParameterTypes = new ArrayList<String>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					operationParameterTypes.add(param.getType().getName());
				}
			}
		}
		return operationParameterTypes;
	}

	public static List<String> getOperationParamNames(Operation operation) {
		List<String> operationParameterTypes = new ArrayList<String>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					operationParameterTypes.add(param.getName());
				}
			}
		}
		return operationParameterTypes;
	}

	public static List<Pair<String, String>> getOperationParams(Operation operation) {
		List<Pair<String, String>> operationParameters = new ArrayList<Pair<String, String>>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					operationParameters.add(new Pair<String, String>(param.getType().getName(), param.getName()));
				} else {
					operationParameters.add(new Pair<String, String>("UNKNOWN_TYPE", param.getName()));
				}
			}
		}
		return operationParameters;
	}

	public static void writeOutSource(String path, String fileName, String source)
			throws FileNotFoundException, UnsupportedEncodingException {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter writer = new PrintWriter(path + File.separator + fileName, "UTF-8");
		writer.println(source);
		writer.close();
	}

	public static String format(String source) {

		CodeFormatter formatter = ToolFactory.createDefaultCodeFormatter(null);
		TextEdit edit = formatter.format(CodeFormatter.K_STATEMENTS, source, 0, source.length(), 0, null);
		IDocument document = new Document(source);
		try {
			edit.apply(document);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		String formattedSource = document.get();

		return formattedSource;
	}

}
