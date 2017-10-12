package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.ToolFactory;
import org.eclipse.cdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.utils.Pair;

public class CppExporterUtils {
	
	private static String UNKNOWN_TYPE = "UNKNOWN_TYPE";
	private static String WRITER_ENCODE = "UTF-8";
	
	@SuppressWarnings("unchecked")
	public static <ElementTypeT, EClassTypeT> void getTypedElements(Collection<ElementTypeT> dest, EClassTypeT eClass, List<Element> elements) {
		for (Element item : elements) {
			if (item.eClass().equals(eClass)) {
				dest.add((ElementTypeT) item);
			}
		}
	}
	
	public static Set<String> getAllModelClassNames(List<Element> elements) {

		Set<String> classNames = new HashSet<String>();
		for (Class cls : getAllModelCLass(elements)) {
			if (!isSignalFactoryClass(cls,elements)) {
				classNames.add(cls.getName());
			}
		}

		return classNames;
	}
	
	public static boolean isConstructor(Operation operation) {

		for (Stereotype stereotype : operation.getAppliedStereotypes()) {
			if (stereotype.getKeyword().equals(ActivityTemplates.CreateStereoType)) {
				return true;

			}
		}
		return false;

	}
	
	public static List<Class> getAllModelCLass(List<Element> elements) {
		List<Class> classes = new ArrayList<Class>();
		getTypedElements(classes, UMLPackage.Literals.CLASS, elements);
		classes.removeIf(c -> isSignalFactoryClass(c, elements));

		return classes;
	}
	
	public static Activity getOperationActivity(Operation operation) {
		Activity activity = null;
		for (Behavior behavior : operation.getMethods()) {

			if (behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
				activity = (Activity) behavior;
				break;
			}
		}

		return activity;
	}
	
	public static List<Pair<String, String>> getOperationParams(Operation operation) {
		List<Pair<String, String>> operationParameters = new ArrayList<Pair<String, String>>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					operationParameters.add(new Pair<String, String>(param.getType().getName(), param.getName()));
				} else {
					operationParameters.add(new Pair<String, String>(UNKNOWN_TYPE, param.getName()));
				}
			}
		}
		return operationParameters;
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

		PrintWriter writer = new PrintWriter(path + File.separator + fileName, WRITER_ENCODE);
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
	
	public static Class getSignalFactoryClass(Signal signal, List<Element> elements) {
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
	
	public static String signalCtrBody(Signal signal, List<Element> elements) {
		ActivityExporter activityExporter = new ActivityExporter();
		Class factoryClass = getSignalFactoryClass(signal, elements);
		String body = "";
		for (Operation operation : factoryClass.getOperations()) {
			if (isConstructor(operation)) {
				body = activityExporter.createFunctionBody(getOperationActivity(operation)).getActivitySource();

			}
		}

		return body;

	}
	
	public static List<Parameter> getSignalConstructorParameters(Signal signal, List<Element> elements) {
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
	public static StateMachine getStateMachine(Class cls) {
	
		List<StateMachine> smList = new ArrayList<StateMachine>();
		getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE, cls.getOwnedElements());
		
		if (!smList.isEmpty()) {
			return smList.get(0);

		} else {
			return null;
		}
	}

	
	private static boolean isSignalFactoryClass(Class cls, List<Element> elements) {
		List<Signal> signals = new ArrayList<Signal>();
		getTypedElements(signals, UMLPackage.Literals.SIGNAL, elements);
		for (Operation op : cls.getOperations()) {
			if (isConstructor(op)) {
				for (Parameter param : op.getOwnedParameters()) {
					if (signals.contains(param.getType())) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public static boolean isStateMachineOwner(Class cls) {
		return CppExporterUtils.getStateMachine(cls) != null;
	}
	
	public static String cutOffTheLastCharcter(String originalString) {
		int originalLeght = originalString.length();
		if(originalLeght == 0) {
			return "";
		}
		return originalString.substring(0, originalLeght - 1);
	}
}
