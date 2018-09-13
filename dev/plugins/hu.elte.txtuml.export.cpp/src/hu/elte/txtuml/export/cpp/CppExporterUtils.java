package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.cdt.core.ToolFactory;
import org.eclipse.cdt.core.formatter.CodeFormatter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.CollectionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.utils.Pair;

public class CppExporterUtils {

	private static String UNKNOWN_TYPE = "UNKNOWN_TYPE";
	private static String WRITER_ENCODE = "UTF-8";
	private static final String OPERATING_SYSTEM = System.getProperty("os.name");

	@SuppressWarnings("unchecked")
	public static <ElementTypeT, EClassTypeT> void getTypedElements(Collection<ElementTypeT> dest, EClassTypeT eClass,
			List<Element> elements) {
		for (Element item : elements) {
			if (item.eClass().equals(eClass)) {
				dest.add((ElementTypeT) item);
			}
		}
	}

	public static Set<String> getAllModelClassNames(List<Element> elements) {

		Set<String> classNames = new HashSet<String>();
		for (Class cls : getAllModelClass(elements)) {
			if (!isSignalFactoryClass(cls, elements)) {
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

	public static List<Class> getAllModelClass(List<Element> elements) {
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

	public static class TypeDescriptor {
		public static final TypeDescriptor NoReturn = new TypeDescriptor(ModifierNames.NoReturn);

		public TypeDescriptor(String typeName, int lowMul, int upMul) {
			super();
			this.typeName = typeName;
			this.upMul = upMul;
			this.lowMul = lowMul;
			isRawType = false;
		}

		public TypeDescriptor(String typeName) {
			super();
			this.typeName = typeName;
			this.upMul = 1;
			this.lowMul = 1;
			isRawType = true;
		}

		public String getTypeName() {
			return typeName;
		}

		public int getUpMul() {
			return upMul;
		}

		public int getLowMul() {
			return lowMul;
		}

		public boolean isRawType() {
			return upMul == 1 && lowMul == 1 && isRawType;
		}

		String typeName;
		int upMul;
		int lowMul;
		boolean isRawType;
	}

	public static List<Pair<TypeDescriptor, String>> getOperationParams(Operation operation) {
		List<Pair<TypeDescriptor, String>> operationParameters = new ArrayList<>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					operationParameters.add(new Pair<TypeDescriptor, String>(
							new TypeDescriptor(param.getType().getName(), param.getLower(), param.getUpper()),
							param.getName()));
				} else {
					operationParameters
							.add(new Pair<TypeDescriptor, String>(new TypeDescriptor(UNKNOWN_TYPE), param.getName()));
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

	public static List<TypeDescriptor> getOperationParamTypes(Operation operation) {
		List<TypeDescriptor> operationParameterTypes = new ArrayList<>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					operationParameterTypes
							.add(new TypeDescriptor(param.getType().getName(), param.getLower(), param.getUpper()));
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

	public static String escapeQuates(String source) {
		StringBuilder resultSource = new StringBuilder("");

		for (char ch : source.toCharArray()) {
			if (ch == '"') {
				resultSource.append("\\");
			}
			resultSource.append(ch);
		}

		return resultSource.toString();
	}

	private static Class getSignalFactoryClass(Signal signal, List<Element> elements) {
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

	public static Map<Signal, Operation> getSignalsWithConstructors(List<Element> elements) {
		Map<Signal, Operation> signalsToConstructorOperations = new HashMap<>();

		List<Signal> signalList = new ArrayList<Signal>();
		CppExporterUtils.getTypedElements(signalList, UMLPackage.Literals.SIGNAL, elements);

		for (Signal signal : signalList) {
			Operation op = null;
			Class factoryClass = getSignalFactoryClass(signal, elements);
			assert (factoryClass != null);
			if (factoryClass != null) {
				for (Operation operation : factoryClass.getOperations()) {
					if (isConstructor(operation)) {
						op = operation;
					}
				}
			}

			assert (op != null);
			if (op != null) {
				signalsToConstructorOperations.put(signal, op);
			}

		}

		return signalsToConstructorOperations;
	}

	public static Optional<StateMachine> getStateMachine(Class cls) {

		List<StateMachine> smList = new ArrayList<StateMachine>();
		getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE, cls.getOwnedElements());

		if (!smList.isEmpty()) {
			return Optional.of(smList.get(0));

		} else {
			return Optional.empty();
		}
	}

	public static boolean isStateMachineOwner(Class cls) {
		return CppExporterUtils.getStateMachine(cls).isPresent();
	}

	public static String getFirstGeneralClassName(Classifier cls) {
		if (!cls.getGeneralizations().isEmpty()) {
			String className = cls.getGeneralizations().get(0).getGeneral().getName();
			return PrivateFunctionalTemplates.mapUMLTypeToCppClass(className);
		} else {
			return GenerationNames.InterfaceNames.EmptyInfName;
		}

	}

	public static String getUsedInterfaceName(Interface inf) {
		EList<Element> modelRoot = inf.getModel().allOwnedElements();
		List<Usage> usages = new ArrayList<>();
		CppExporterUtils.getTypedElements(usages, UMLPackage.Literals.USAGE, modelRoot);

		Optional<Usage> infOptionalUsage = usages.stream().filter(u -> u.getClients().contains(inf)).findFirst();
		if (infOptionalUsage.isPresent()) {
			Usage infUsage = infOptionalUsage.get();
			if (infUsage.getSuppliers().isEmpty()) {
				return GenerationNames.InterfaceNames.EmptyInfName;
			}
			return PrivateFunctionalTemplates.mapUMLTypeToCppClass(infUsage.getSuppliers().get(0).getName());
		} else {
			return GenerationNames.InterfaceNames.EmptyInfName;
		}
	}

	public static String createTemplateParametersCode(Optional<List<String>> templateParamOptionalList) {
		String source = "";
		if (templateParamOptionalList.isPresent()) {
			List<String> templateParameters = templateParamOptionalList.get();
			if (!templateParameters.isEmpty()) {
				source = "<" + enumerateListElementsCode(templateParameters) + ">";
			}
		}

		return source.toString();
	}

	public static String createParametersCode(Optional<List<String>> optionalParams) {
		String source = "";
		if (optionalParams.isPresent()) {
			List<String> params = optionalParams.get();
			if (!params.isEmpty()) {
				source = "(" + enumerateListElementsCode(params) + ")";
			}
		}

		return source.toString();
	}

	public static String enumerateListElementsCode(List<String> list) {
		assert (list != null);
		StringBuilder source = new StringBuilder("");
		for (String elem : list) {
			source.append(elem + ",");
		}
		return cutOffTheLastCharacter(source.toString());
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

	public static String cutOffTheLastCharacter(String originalString) {
		int originalLeght = originalString.length();
		if (originalLeght == 0) {
			return "";
		}
		return originalString.substring(0, originalLeght - 1);
	}

	public static int executeCommand(String directory, List<String> strings, Map<String, String> environment,
			String fileNameToRedirect) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(strings);
		if (environment != null) {
			processBuilder.environment().putAll(environment);
		}

		processBuilder.inheritIO();
		processBuilder.directory(new File(directory));

		if (fileNameToRedirect != null) {
			processBuilder = processBuilder.redirectOutput(new File(directory + "/" + fileNameToRedirect));
		}
		Process process = processBuilder.start();
		return process.waitFor();
	}
	
	public static String oneReadReference(String varName) {
		return ActivityTemplates.operationCall(varName, PointerAndMemoryNames.SimpleAccess, 
				CollectionNames.SelectAnyFunctionName, Collections.emptyList());
	}

	public static boolean isWindowsOS() {
		return OPERATING_SYSTEM.toUpperCase().startsWith("WIN");
	}
	
	public static String qualifiedNameToSimpleName(String qualifiedName) {
		String[] nameParts = qualifiedName.split("\\.");
		return nameParts[nameParts.length - 1];
	}

}