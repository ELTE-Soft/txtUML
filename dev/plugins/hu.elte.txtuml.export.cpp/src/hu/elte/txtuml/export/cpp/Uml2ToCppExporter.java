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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.thread.ThreadPoolConfiguration;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.Options;
import hu.elte.txtuml.export.cpp.thread.ThreadHandlingManager;
import hu.elte.txtuml.utils.Pair;

public class Uml2ToCppExporter {
	public static final String GENERATED_CPP_FOLDER_NAME = "cpp-gen";
	public static final String UML_FILES_FOLDER_NAME = "model";

	private static final String RUNTIME_DIR_PREFIX = GenerationTemplates.RuntimePath;
	private static final String RUNTIME_LIB_NAME = "libsmrt";
	private static final String DEFAULT_TARGET_EXECUTABLE = "main";
	private static final String DEFAULT_DEPLOYMENT_NAME = "deployment";
	private static final String DEFAULT_ASSOCIATIONS_NAME = "associations";
	private static final String PROJECT_NAME = "hu.elte.txtuml.export.cpp";
	private static final String CPP_FILES_FOLDER_NAME = "cpp-runtime";

	private ClassExporter classExporter;
	private final Options options;

	ThreadHandlingManager threadManager;
	
	private Map<Class,String> generatedClassNames;
	int generatedClassCounter;

	Set<Class> classList;
	EList<Element> elements;

	List<String> classNames;

	public Uml2ToCppExporter(Model model, Map<String, ThreadPoolConfiguration> threadDescription,
			boolean addRuntimeOption, boolean overWriteMainFileOption) {
		classExporter = new ClassExporter();

		this.classList = new HashSet<Class>();
		this.elements = model.allOwnedElements();
		this.classNames = new LinkedList<String>();
		this.generatedClassNames = new HashMap<Class,String>();
		generatedClassCounter = 0;

		Shared.getTypedElements(classList, elements, UMLPackage.Literals.CLASS);

		options = new Options(addRuntimeOption, overWriteMainFileOption);
		threadManager = new ThreadHandlingManager(threadDescription);
	}

	public void buildCppCode(String outputDirectory) throws IOException {

		if (options.isAddRuntime()) {
			threadManager.createConfigurationSource(outputDirectory);
		}

		Shared.writeOutSource(outputDirectory, (GenerationTemplates.EventHeader), createEventSource(elements));


		copyPreWrittenCppFiles(outputDirectory);

		for (Class item : classList) {
		    
		    if(threadManager.getDescription().get(item.getName()) != null && !Shared.generatedClass(item)) {
		    	
			classExporter.reiniIialize();
			classExporter.setConfiguratedPoolId(threadManager.getDescription().get(item.getName()).getId());
			classExporter.setRealName(getRealClassName(item));

			classExporter.createSource(item, outputDirectory);

			classNames.addAll(classExporter.getSubmachines());
			classNames.add(getRealClassName(item));
			classNames.addAll(classExporter.getAdditionalSources());
		    }

		}

		createCMakeFile(outputDirectory);
		createAssociationsSources(outputDirectory);
	}


	
	private String getRealClassName(Class cls) {
		return generatedClassNames.containsKey(cls) ? generatedClassNames.get(cls) : cls.getName();
	}
	
	private void copyPreWrittenCppFiles(String destination) throws IOException {

		String cppFilesLocation = seekCppFilesLocation();
		File file = new File(destination);
		if (!file.exists()) {
			file.mkdirs();
		}

		Files.copy(Paths.get(cppFilesLocation + GenerationTemplates.StateMachineBaseHeader),
				Paths.get(destination + File.separator + GenerationTemplates.StateMachineBaseHeader),
				StandardCopyOption.REPLACE_EXISTING);
		if (options.isAddRuntime()) {

			File sourceRuntimeDir = new File(cppFilesLocation);
			File outputRuntimeDir = new File(destination + File.separator + RUNTIME_DIR_PREFIX);
			if (!outputRuntimeDir.exists()) {
				outputRuntimeDir.mkdirs();
			}

			copyFolder(sourceRuntimeDir, outputRuntimeDir);

		}
		
		
		
		if(!Paths.get(destination + File.separator + "main.cpp").toFile().exists() || options.isOverWriteMainFile()) {
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
		sourceNames.addAll(classNames);
		cmake.addExecutableTarget(DEFAULT_TARGET_EXECUTABLE, sourceNames, "");
		cmake.writeOutCMakeLists();
	}

	private String createEventSource(EList<Element> elements_) {
		List<Signal> signalList = new ArrayList<Signal>();
		Shared.getTypedElements(signalList, elements_, UMLPackage.Literals.SIGNAL);
		StringBuilder forwardDecl = new StringBuilder("");
		StringBuilder events = new StringBuilder("");
		StringBuilder source = new StringBuilder("");
		List<Pair<String, String>> allParam = new LinkedList<Pair<String, String>>();
		
		events.append("InitSignal_EE,");
		for (Signal item : signalList) {
			List<Pair<String, String>> currentParams = getSignalParams(item);
			String ctrBody = Shared.signalCtrBody(item, elements_);
			allParam.addAll(currentParams);
			source.append(GenerationTemplates.eventClass(item.getName(), 
					 currentParams,ctrBody,item.getOwnedAttributes(),options));
			events.append(item.getName() + "_EE,");
		}
		events = new StringBuilder(events.substring(0, events.length()-1));

		source.append(GenerationTemplates.eventClass("InitSignal", 
				new ArrayList<Pair<String, String>>(),"",new ArrayList<Property>(),options));

		for (Pair<String, String> param : allParam) {
			if (!Shared.isBasicType(param.getFirst())) {
				String tmp = GenerationTemplates.forwardDeclaration(param.getFirst());
				// TODO this is suboptimal
				if (!forwardDecl.toString().contains(tmp)) {
					forwardDecl.append(tmp);
				}
			}
		}
		
		forwardDecl.append( GenerationTemplates.eventBase(options).append("\n"));
		forwardDecl.append("enum Events {" +  events + "};\n");
		forwardDecl.append(source);
		return GenerationTemplates.eventHeaderGuard(forwardDecl.toString());
	}
	
	private void createAssociationsSources(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {		
		
		Set<String> associatedClasses = new HashSet<String>();
		StringBuilder includes = new StringBuilder
				(GenerationTemplates.cppInclude(GenerationTemplates.AssociationsStructuresHreaderName));
		StringBuilder preDeclerations = new StringBuilder("");
		StringBuilder structures = new StringBuilder("");
		StringBuilder functions = new StringBuilder("");
		
		List<Association> associationList = new ArrayList<Association>();
		Shared.getTypedElements(associationList, elements, UMLPackage.Literals.ASSOCIATION);
		for(Association assoc: associationList) {
			Property e1End = assoc.getMemberEnds().get(0);
			Property e2End = assoc.getMemberEnds().get(1);
			String e1 = e1End.getType().getName();
			String e1Name = e1End.getName();
			String e2 = e2End.getType().getName();
			String e2Name = e2End.getName();
			associatedClasses.add(e1);
			associatedClasses.add(e2);
		    structures.append(GenerationTemplates.createAssociationStructure
		    		(assoc.getName(),e1,e2,e1Name,e2Name));
		    
		    functions.append(GenerationTemplates.linkTemplateSpecializationDef(e1,
					e2, assoc.getName(), e2Name, e2End.isNavigable(),
					GenerationTemplates.LinkFunctionType.Link));
		    functions.append(GenerationTemplates.linkTemplateSpecializationDef(e2,
					e1, assoc.getName(), e1Name, e1End.isNavigable(),
					GenerationTemplates.LinkFunctionType.Link));
		    
		    functions.append(GenerationTemplates.linkTemplateSpecializationDef(e2,
					e1, assoc.getName(), e1Name, e1End.isNavigable(),
					GenerationTemplates.LinkFunctionType.Unlink));		    
		    functions.append(GenerationTemplates.linkTemplateSpecializationDef(e1,
					e2, assoc.getName(), e2Name, e2End.isNavigable(),
					GenerationTemplates.LinkFunctionType.Unlink));

		    
		    
		    
		}
		
		for(String className : associatedClasses) {
			includes.append(GenerationTemplates.cppInclude(className));
			preDeclerations.append(GenerationTemplates.forwardDeclaration(className));
		}
		
		Shared.writeOutSource(outputDirectory, (GenerationTemplates.AssociationStructuresHeader), 
				GenerationTemplates.headerGuard
				(GenerationTemplates.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.AssocationHeader)	
						+ preDeclerations.toString() + structures.toString(), 
						GenerationTemplates.AssociationsStructuresHreaderName));
		Shared.writeOutSource(outputDirectory, (GenerationTemplates.AssociationStructuresSource), includes.toString() + functions.toString());
				
		
	}

	private List<Pair<String, String>> getSignalParams(Signal signal) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param : Shared.getSignalConstructorParameters(signal,elements)) {
			ret.add(new Pair<String, String>(param.getType().getName(), param.getName()));
		}
		return ret;
	}
}
