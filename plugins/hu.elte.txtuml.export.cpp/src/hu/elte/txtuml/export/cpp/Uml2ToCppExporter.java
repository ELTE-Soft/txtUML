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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.thread.ThreadPoolConfiguration;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.Options;
import hu.elte.txtuml.export.cpp.thread.ThreadHandlingManager;
import hu.elte.txtuml.utils.Pair;

public class Uml2ToCppExporter {
	private static final String DefaultCompiler = "g++";
	private static final String RuntimeFolder = GenerationTemplates.RuntimePath;
	private static final String RuntimeLibName = "libsmrt.a";
	private static final String DefaultMakeFileName = "Makefile";
	private static final String DefaultModelName = "main";
	private static final String ProjectName = "hu.elte.txtuml.export.cpp";
	private static final String CppFilesFolderName = "cpp-runtime";

	private ClassExporter classExporter;
	private final Options options;

	ThreadHandlingManager threadManager;

	List<Class> classList;
	EList<Element> elements;

	List<String> classNames;

	public Uml2ToCppExporter(Model model, Map<String, ThreadPoolConfiguration> threadDescription,
			boolean addRuntimeOption) {
		classExporter = new ClassExporter();

		this.classList = new ArrayList<Class>();
		this.elements = model.allOwnedElements();
		this.classNames = new LinkedList<String>();

		Shared.getTypedElements(classList, elements, UMLPackage.Literals.CLASS);

		options = new Options(addRuntimeOption);
		threadManager = new ThreadHandlingManager(classList, threadDescription);
	}

	public void buildCppCode(String outputDirectory) throws IOException {

		if (options.isAddRuntime()) {
			threadManager.createThreadPoolManager(outputDirectory + File.separator + "runtime");
		}

		Shared.writeOutSource(outputDirectory, (GenerationTemplates.EventHeader), createEventSource(elements));

		copyPreWrittenCppFiles(outputDirectory);

		for (Class item : classList) {
			classExporter.reiniIialize();
			classExporter.setConfiguratedPoolId(threadManager.getDescription().get(item.getName()).getId());

			classExporter.createSource(item, outputDirectory);

			classNames.addAll(classExporter.getSubmachines());
			classNames.add(item.getName());
		}

		createMakeFile(outputDirectory, DefaultModelName, DefaultMakeFileName, classNames);

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
			File outputRuntimeDir = new File(destination + File.separator + RuntimeFolder);
			if (!outputRuntimeDir.exists()) {
				outputRuntimeDir.mkdirs();
			}

			copyFolder(sourceRuntimeDir, outputRuntimeDir);

		}
		Files.copy(Paths.get(cppFilesLocation + "main.cpp"), Paths.get(destination + File.separator + "main.cpp"),
				StandardCopyOption.REPLACE_EXISTING);

	}

	private String seekCppFilesLocation() throws IOException {

		Bundle bundle = Platform.getBundle(ProjectName);
		URL fileURL = bundle.getEntry(CppFilesFolderName);
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

	private void createMakeFile(String path_, String outputName_, String makefileName_, List<String> classNames_)
			throws FileNotFoundException, UnsupportedEncodingException {
		String makeFile = "CC=" + DefaultCompiler + "\n\nall: " + outputName_ + "\n\n";

		makeFile += outputName_ + ":";
		if (options.isAddRuntime()) {
			makeFile += " " + RuntimeLibName;
		}

		String fileList = " main.cpp";
		for (String file : classNames_) {
			fileList += " " + GenerationTemplates.sourceName(file);
		}

		makeFile += fileList + "\n";
		makeFile += "\t$(CC)";
		makeFile += " -Wall -o " + outputName_ + fileList + " -std=gnu++11";

		if (options.isAddRuntime()) {
			makeFile += " -I " + RuntimeFolder + " -LC " + RuntimeLibName + " -pthread\n\n" + RuntimeLibName
					+ ": runtime runtime.o statemachineI.o threadpool.o threadpoolmanager.o threadcontainer.o threadconfiguration.o\n"
					+ "\tar rcs " + RuntimeLibName
					+ " runtime.o statemachineI.o threadpool.o threadpoolmanager.o threadcontainer.o threadconfiguration.o\n\n"
					+ ".PHONY:runtime\n" + "runtime:\n\t$(CC) -Wall -c " + RuntimeFolder + "runtime.cpp "
					+ RuntimeFolder + "statemachineI.cpp " + RuntimeFolder + "threadpool.cpp " + RuntimeFolder
					+ "threadpoolmanager.cpp " + RuntimeFolder + "threadcontainer.cpp" + RuntimeFolder
					+ "threadconfiguration.cpp" + "-std=gnu++11";
		}

		Shared.writeOutSource(path_, makefileName_, makeFile);
	}

	private String createEventSource(EList<Element> elements_) {
		List<Signal> signalList = new ArrayList<Signal>();
		Shared.getTypedElements(signalList, elements_, UMLPackage.Literals.SIGNAL);
		String forwardDecl = "";
		String source = GenerationTemplates.eventBase(options) + "\n";
		List<Pair<String, String>> allParam = new LinkedList<Pair<String, String>>();

		for (Signal item : signalList) {
			List<Pair<String, String>> currentParams = getSignalParams(item);
			allParam.addAll(currentParams);
			source += GenerationTemplates.eventClass(item.getName(), currentParams, options);
		}

		source += GenerationTemplates.eventClass("InitSignal", new ArrayList<Pair<String, String>>(), options);

		for (Pair<String, String> param : allParam) {
			if (!Shared.isBasicType(param.getFirst())) {
				String tmp = GenerationTemplates.forwardDeclaration(param.getFirst());
				if (!forwardDecl.contains(tmp)) {
					forwardDecl += tmp;
				}
			}
		}
		forwardDecl += "\n";
		source = forwardDecl + source;
		return GenerationTemplates.eventHeaderGuard(source);
	}

	private List<Pair<String, String>> getSignalParams(Signal signal_) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Property prop : signal_.getOwnedAttributes()) {
			ret.add(new Pair<String, String>(prop.getType().getName(), prop.getName()));
		}
		return ret;
	}
}
