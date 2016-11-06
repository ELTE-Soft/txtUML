package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.thread.ThreadPoolConfiguration;
import hu.elte.txtuml.export.cpp.structural.ClassExporter;
import hu.elte.txtuml.export.cpp.structural.DataTypeExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.Options;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.export.cpp.thread.ThreadHandlingManager;
import hu.elte.txtuml.export.cpp.structural.DependencyExporter;

import hu.elte.txtuml.utils.Pair;

public class Uml2ToCppExporter {
	public static final String GENERATED_CPP_FOLDER_NAME = "cpp-gen";
	public static final String UML_FILES_FOLDER_NAME = "model";

	private static final String RUNTIME_DIR_PREFIX = RuntimeTemplates.RTPath;
	private static final String RUNTIME_LIB_NAME = "libsmrt";
	private static final String DEFAULT_TARGET_EXECUTABLE = "main";
	private static final String DEFAULT_DEPLOYMENT_NAME = "deployment";
	private static final String DEFAULT_ASSOCIATIONS_NAME = "associations";
	private static final String PROJECT_NAME = "hu.elte.txtuml.export.cpp";
	private static final String CPP_FILES_FOLDER_NAME = "cpp-runtime";
	private static final String ENUM_EXTENSION = "_EE";
	private static final String DEFAULT_INIT_MACHINE_NAME = StateMachineTemplates.TransitionTableInitialSourceName;

	private ClassExporter classExporter;
	private DataTypeExporter dataTypeExporter;
	private Shared shared;
	private final Options options;

	private ThreadHandlingManager threadManager;

	private List<Class> classes;
	private List<String> stateMachineOwners;
	private List<DataType> dataTypes;
	private List<String> classNames;

	public Uml2ToCppExporter(Shared shared, Map<String, ThreadPoolConfiguration> threadDescription,
			boolean addRuntimeOption, boolean overWriteMainFileOption) {
		classExporter = new ClassExporter();
		dataTypeExporter = new DataTypeExporter();
		threadManager = new ThreadHandlingManager(threadDescription);

		classes = new ArrayList<Class>();
		dataTypes = new ArrayList<DataType>();
		classNames = new LinkedList<String>();
		stateMachineOwners = new ArrayList<String>();
		options = new Options(addRuntimeOption, overWriteMainFileOption);

		this.shared = shared;
		shared.getTypedElements(dataTypes, UMLPackage.Literals.DATA_TYPE);
		classes = shared.getAllModelCLass();

	}

	public void buildCppCode(String outputDirectory) throws IOException {

		threadManager.createConfigurationSource(outputDirectory);

		copyPreWrittenCppFiles(outputDirectory);
		createEventSource(outputDirectory);
		createClassSources(outputDirectory);
		createTransitionTableInitialSource(outputDirectory);
		createDataTypes(outputDirectory);
		createAssociationsSources(outputDirectory);
		createCMakeFile(outputDirectory);
	}

	private void createClassSources(String outputDirectory) throws IOException {
		for (Class cls : classes) {

			classExporter.setName(cls.getName());
			classExporter.setPoolId(threadManager.getDescription().get(cls.getName()).getId());
			classExporter.exportStructuredElement(cls, outputDirectory);

			classNames.addAll(classExporter.getSubmachines());
			classNames.add(cls.getName());
			classNames.addAll(classExporter.getAdditionalSources());

			if (classExporter.isStateMachineOwner()) {
				stateMachineOwners.add(cls.getName());
				stateMachineOwners.addAll(classExporter.getSubmachines());
			}

		}
	}

	private void createTransitionTableInitialSource(String outputDirectory) throws IOException {
		Shared.writeOutSource(outputDirectory,
				GenerationTemplates
						.headerName(StateMachineTemplates.TransitionTableInitialSourceName),
				Shared.format(
						HeaderTemplates
								.headerGuard(
										GenerationTemplates.putNamespace(
												FunctionTemplates.functionDecl(
														StateMachineTemplates.AllTransitionTableInitialProcName),
												StateMachineTemplates.MachineNamespace),
										StateMachineTemplates.TransitionTableInitialSourceName)));

		DependencyExporter dependencyExporter = new DependencyExporter();
		dependencyExporter.addDependecies(stateMachineOwners);
		StringBuilder initalFunctionBody = new StringBuilder("");
		for (String stateMachineOwner : stateMachineOwners) {
			initalFunctionBody.append(ActivityTemplates.blockStatement(GenerationTemplates
					.staticMethodInvoke(stateMachineOwner, StateMachineTemplates.InitTransitionTable)));
		}

		Shared.writeOutSource(outputDirectory,
				GenerationTemplates.sourceName(StateMachineTemplates.TransitionTableInitialSourceName),
				Shared.format(dependencyExporter.createDependencyCppIncudesCode(
						StateMachineTemplates.TransitionTableInitialSourceName)
						+ GenerationTemplates.putNamespace(
								FunctionTemplates.simpleFunctionDef(GenerationNames.NoReturn,
										StateMachineTemplates.AllTransitionTableInitialProcName,
										initalFunctionBody.toString(),""),StateMachineTemplates.MachineNamespace)));

	}

	private void createDataTypes(String outputDirectory) throws IOException {
		for (DataType dataType : dataTypes) {
			dataTypeExporter.setName(dataType.getName());
			dataTypeExporter.init();
			dataTypeExporter.exportStructuredElement(dataType, outputDirectory);
		}

	}

	private void copyPreWrittenCppFiles(String destination) throws IOException {

		String cppFilesLocation = seekCppFilesLocation();
		File file = new File(destination);
		if (!file.exists()) {
			file.mkdirs();
		}

		Files.copy(Paths.get(cppFilesLocation + StateMachineTemplates.StateMachineBaseHeader),
				Paths.get(destination + File.separator + StateMachineTemplates.StateMachineBaseHeader),
				StandardCopyOption.REPLACE_EXISTING);
		if (options.isAddRuntime()) {

			File sourceRuntimeDir = new File(cppFilesLocation);
			File outputRuntimeDir = new File(destination + File.separator + RUNTIME_DIR_PREFIX);
			if (!outputRuntimeDir.exists()) {
				outputRuntimeDir.mkdirs();
			}

			copyFolder(sourceRuntimeDir, outputRuntimeDir);

		}

		if (!Paths.get(destination + File.separator + "main.cpp").toFile().exists() || options.isOverWriteMainFile()) {
			Files.copy(Paths.get(cppFilesLocation + "main.cpp"), Paths.get(destination + File.separator + "main.cpp"),
					StandardCopyOption.REPLACE_EXISTING);
		}

	}

	private String seekCppFilesLocation() throws IOException {

		Bundle bundle = Platform.getBundle(PROJECT_NAME);
		URL fileURL = bundle.getEntry(CPP_FILES_FOLDER_NAME);
		File f = new File(FileLocator.toFileURL(fileURL).getPath());

		return f.getPath() + File.separator;
	}

	private void copyFolder(File sourceRuntimeDir, File outputRuntimeDir) throws IOException {

		String files[] = sourceRuntimeDir.list();

		for (String file : files) {
			Files.copy(Paths.get(sourceRuntimeDir.getAbsolutePath() + File.separator + file),
					Paths.get(outputRuntimeDir.getAbsolutePath() + File.separator + file),
					StandardCopyOption.REPLACE_EXISTING);
		}

	}

	private void createCMakeFile(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
		CMakeSupport cmake = new CMakeSupport(outputDirectory);
		cmake.addIncludeDirectory(RUNTIME_DIR_PREFIX.substring(0, RUNTIME_DIR_PREFIX.indexOf(File.separator)));
		List<String> librarySourceClasses = new ArrayList<String>();
		librarySourceClasses.add("runtime");
		librarySourceClasses.add("statemachineI");
		librarySourceClasses.add("threadpool");
		librarySourceClasses.add("threadpoolmanager");
		librarySourceClasses.add("threadcontainer");
		librarySourceClasses.add("threadconfiguration");
		librarySourceClasses.add("standard_functions");
		librarySourceClasses.add("timer");
		librarySourceClasses.add("itimer");
		cmake.addStaticLibraryTarget(RUNTIME_LIB_NAME, librarySourceClasses, RUNTIME_DIR_PREFIX);
		List<String> sourceNames = new ArrayList<String>();
		sourceNames.add(DEFAULT_TARGET_EXECUTABLE);
		sourceNames.add(DEFAULT_DEPLOYMENT_NAME);
		sourceNames.add(DEFAULT_ASSOCIATIONS_NAME);
		sourceNames.add(DEFAULT_INIT_MACHINE_NAME);
		sourceNames.addAll(classNames);
		cmake.addExecutableTarget(DEFAULT_TARGET_EXECUTABLE, sourceNames, "");
		cmake.writeOutCMakeLists();
	}

	private void createEventSource(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
		List<Signal> signalList = new ArrayList<Signal>();
		shared.getTypedElements(signalList, UMLPackage.Literals.SIGNAL);
		StringBuilder forwardDecl = new StringBuilder("");
		StringBuilder events = new StringBuilder("");
		StringBuilder source = new StringBuilder("");
		List<Pair<String, String>> allParam = new LinkedList<Pair<String, String>>();

		events.append(EventTemplates.InitSignal + ENUM_EXTENSION + ",");
		for (Signal signal : signalList) {
			List<Pair<String, String>> currentParams = getSignalParams(signal);
			String ctrBody = shared.signalCtrBody(signal);
			allParam.addAll(currentParams);
			source.append(
					EventTemplates.eventClass(signal.getName(), currentParams, ctrBody, signal.getOwnedAttributes()));
			events.append(signal.getName() + ENUM_EXTENSION + ",");
		}
		events = new StringBuilder(events.substring(0, events.length() - 1));

		source.append(EventTemplates.eventClass(EventTemplates.InitSignal, new ArrayList<Pair<String, String>>(), "",
				new ArrayList<Property>()));

		DependencyExporter dependencyEporter = new DependencyExporter();
		for (Pair<String, String> param : allParam) {
			dependencyEporter.addDependecy(param.getSecond());
		}
		
		
		forwardDecl.append(dependencyEporter.createDependecyHeaderIncludeCode());
		forwardDecl.append(RuntimeTemplates.eventHeaderInclude());
		forwardDecl.append("enum Events {" + events + "};\n");
		forwardDecl.append(source);
		Shared.writeOutSource(outputDirectory, (EventTemplates.EventHeader),
				Shared.format(EventTemplates.eventHeaderGuard(forwardDecl.toString())));

	}

	private void createAssociationsSources(String outputDirectory)
			throws FileNotFoundException, UnsupportedEncodingException {

		Set<String> associatedClasses = new HashSet<String>();
		StringBuilder includes = new StringBuilder(
				GenerationTemplates.cppInclude(LinkTemplates.AssociationsStructuresHreaderName));
		StringBuilder preDeclerations = new StringBuilder("");
		StringBuilder structures = new StringBuilder("");
		StringBuilder functions = new StringBuilder("");

		List<Association> associationList = new ArrayList<Association>();
		shared.getTypedElements(associationList, UMLPackage.Literals.ASSOCIATION);
		for (Association assoc : associationList) {
			Property e1End = assoc.getMemberEnds().get(0);
			Property e2End = assoc.getMemberEnds().get(1);
			String e1 = e1End.getType().getName();
			String e1Name = e1End.getName();
			String e2 = e2End.getType().getName();
			String e2Name = e2End.getName();
			associatedClasses.add(e1);
			associatedClasses.add(e2);
			structures.append(LinkTemplates.createAssociationStructure(assoc.getName(), e1, e2, e1Name, e2Name));

			functions.append(LinkTemplates.linkTemplateSpecializationDef(e1, e2, assoc.getName(), e2Name,
					e2End.isNavigable(), LinkTemplates.LinkFunctionType.Link));
			functions.append(LinkTemplates.linkTemplateSpecializationDef(e2, e1, assoc.getName(), e1Name,
					e1End.isNavigable(), LinkTemplates.LinkFunctionType.Link));

			functions.append(LinkTemplates.linkTemplateSpecializationDef(e2, e1, assoc.getName(), e1Name,
					e1End.isNavigable(), LinkTemplates.LinkFunctionType.Unlink));
			functions.append(LinkTemplates.linkTemplateSpecializationDef(e1, e2, assoc.getName(), e2Name,
					e2End.isNavigable(), LinkTemplates.LinkFunctionType.Unlink));

		}

		for (String className : associatedClasses) {
			includes.append(GenerationTemplates.cppInclude(className));
			preDeclerations.append(GenerationTemplates.forwardDeclaration(className));
		}

		Shared.writeOutSource(outputDirectory, (LinkTemplates.AssociationStructuresHeader),
				Shared.format(HeaderTemplates.headerGuard(
						GenerationTemplates.cppInclude(RuntimeTemplates.RTPath + LinkTemplates.AssocationHeader)
								+ preDeclerations.toString() + structures.toString(),
						LinkTemplates.AssociationsStructuresHreaderName)));
		Shared.writeOutSource(outputDirectory, (LinkTemplates.AssociationStructuresSource),
				Shared.format(includes.toString() + functions.toString()));

	}

	private List<Pair<String, String>> getSignalParams(Signal signal) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param : shared.getSignalConstructorParameters(signal)) {
			ret.add(new Pair<String, String>(param.getType().getName(), param.getName()));
		}
		return ret;
	}
}
