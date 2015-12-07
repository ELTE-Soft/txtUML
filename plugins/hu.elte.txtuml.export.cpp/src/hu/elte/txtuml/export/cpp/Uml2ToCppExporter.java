package hu.elte.txtuml.export.cpp;

/***********************************************************
 * Author: Hack J�nos
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 * upcoming features:
 * 	TODO -logging
 *  TODO -?exceptions, depending on design 
 *  TODO -redesign dependency analysis and include generation
 * 	TODO -refactoring more
 **********************************************************/


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

import hu.elte.txtuml.export.cpp.description.ThreadPoolConfiguration;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.Options;
import hu.elte.txtuml.export.cpp.thread.ThreadHandlingManager;
import hu.elte.txtuml.utils.Pair;

public class Uml2ToCppExporter
{
	private static final String DefaultCompiler = "g++";
	private static final String GCCDebugSymbolOn = "-D_DEBUG"; //the symbol located in GenerationNames
	//private static final String CppFileLocation= "hu.elte.txtuml.export.cpp" + File.separator + "src" +File.separator+"hu"+File.separator+"elte"+File.separator+"txtuml"+File.separator+"export"+File.separator+"cpp"+File.separator+"cppsources"+File.separator;
	//private static final String RuntimeCppFileLocation = CppFileLocation + RuntimeFolder;
	private static final String RuntimeFolder= GenerationTemplates.RuntimePath;
	private static final String RuntimeLibName = "libsmrt.a";
	private static final String DefaultMakeFileName = "Makefile";
	private static final String DefaultModelName = "main";
	
	private Model model;
	
	
	private ClassExporter classExporter;
	
	
	ThreadHandlingManager threadManager;
	
	List<Class> classList;
	List<String> classNames;
	
	
	
	public Uml2ToCppExporter(Model model, Map<String, ThreadPoolConfiguration > threadDescription,
			boolean threadManagement,boolean runtimeOption,boolean debugOption){
		this.model = model;
		
		classExporter = new ClassExporter();
		
		classList = new ArrayList<Class>();
		classNames = new LinkedList<String>();
		
		
		if(runtimeOption){
			Options.setRuntime();
		}
		else{
			Options.setRuntime(false);
		}
		if(debugOption){
			Options.setDebugLog();
		}
		else{
			Options.setDebugLog(false);
		}
		if(threadManagement){
			Options.setThreadManagement();
			threadManager = new ThreadHandlingManager(model,threadDescription);
		}
		else{
			threadManager = new ThreadHandlingManager();
			Options.setThreadManagement(false);
		}
		
	}
	
	
	public void buildCppCode(String outputDirectory) {

		try {
			
			
			threadManager.createThreadPoolManager(outputDirectory + File.separator + "runtime");
			
			EList<Element> elements = model.allOwnedElements();
			
			Shared.writeOutSource(outputDirectory,(GenerationTemplates.EventHeader),createEventSource(elements));
			
			copyPreWrittenCppFiles(outputDirectory);

			
			Shared.getTypedElements(classList,elements,UMLPackage.Literals.CLASS);
			
			
			for(Class item: classList)
			{
				classExporter.reiniIialize();
				classExporter.createSource(item, outputDirectory);
				
				classNames.addAll(classExporter.getSubmachines());
				classNames.add(item.getName());
			}
			
			createMakeFile(outputDirectory,DefaultModelName,DefaultMakeFileName,classNames);
			
			
			System.out.println("Compile completed");
		} catch(IOException e) {
			System.out.println("\nCan not write out generated source the problem:");
			System.out.println(e.getMessage());
        }
	}
	



	private void copyPreWrittenCppFiles(String destination) throws IOException 
	{
		
		String absoluteCppFilesLocation = seekCppFilesColcations();
		
		try
		{
		
			File file = new File(destination);
			if(!file.exists()){
				file.mkdirs();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Files.copy(Paths.get(absoluteCppFilesLocation + GenerationTemplates.StateMachineBaseHeader),
				Paths.get(destination+File.separator+GenerationTemplates.StateMachineBaseHeader), StandardCopyOption.REPLACE_EXISTING);
		if(Options.Runtime())
		{
			
			File sourceRuntimeDir = new File(absoluteCppFilesLocation + RuntimeFolder);
			File outputRuntimeDir = new File(destination + File.separator +  RuntimeFolder);
			if(!outputRuntimeDir.exists()){
				outputRuntimeDir.mkdirs();
			}
				
			Map<String, String> fileNamesMap = new HashMap<String,String>();// the map contains  filenames which will be changed compared to source files
			fileNamesMap.put("runtimemain.cpp", "main.cpp");  
				
			copyFolder(sourceRuntimeDir, outputRuntimeDir, fileNamesMap);
				
		}
		else
		{
			Files.copy(Paths.get(absoluteCppFilesLocation + "main.cpp"),
					Paths.get(destination + File.separator+"main.cpp"), StandardCopyOption.REPLACE_EXISTING);
		}
		
	}
	
	private String seekCppFilesColcations(){
		
		Bundle bundle = Platform.getBundle("hu.elte.txtuml.export.cpp");
		URL fileURL = bundle.getEntry("src" +File.separator+"hu"+File.separator+"elte"+File.separator+"txtuml"+File.separator+"export"+File.separator+"cpp"+File.separator+"cppsources");
		File f = null;
		try {
			f = new File(FileLocator.toFileURL(fileURL).getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f.getAbsolutePath() + File.separator;
	}

	private void copyFolder(File sourceRuntimeDir, File outputRuntimeDir, Map<String, String> fileNamesMap) {
		
		String files[] = sourceRuntimeDir.list();
		
		for(String file: files){
			
			if(fileNamesMap.containsKey(file)){
				try {
					Files.copy(Paths.get(sourceRuntimeDir.getAbsolutePath() + File.separator + file),
							Paths.get(outputRuntimeDir.getAbsolutePath() + File.separator + fileNamesMap.get(file)), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					Files.copy(Paths.get(sourceRuntimeDir.getAbsolutePath() + File.separator + file),
							Paths.get(outputRuntimeDir.getAbsolutePath() + File.separator + file), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	private void createMakeFile(String path_,String outputName_,String makefileName_,List<String> classNames_) throws FileNotFoundException, UnsupportedEncodingException
	{
		String makeFile="CC="+DefaultCompiler+"\n\nall: "+outputName_+"\n\n";
		
		makeFile+=outputName_+":";
		if(Options.Runtime())
		{
			makeFile += " "+ RuntimeLibName;
		}
		
		String fileList=" main.cpp";
		for(String file:classNames_)
		{
			fileList+=" "+GenerationTemplates.SourceName(file);
		}
		

		makeFile+=fileList+"\n";
		makeFile+="\t$(CC)";		
		if(Options.DebugLog())
		{
			makeFile+=" "+GCCDebugSymbolOn;
		}
		makeFile+=" -Wall -o "+outputName_+fileList+" -std=gnu++11";

		if(Options.Runtime())
		{
			makeFile+=" -I "+RuntimeFolder+" -LC "+RuntimeLibName+"\n\n"+RuntimeLibName+": runtime runtime.o statemachineI.o threadpool.o threadpoolmanager.o threadcontainer.o\n"+
					"\tar rcs "+RuntimeLibName+" runtime.o statemachineI.o threadpool.o threadpoolmanager.o threadcontainer.o\n\n"+
					".PHONY:runtime\n"+
					"runtime:\n\t$(CC) -Wall -c "+RuntimeFolder+"runtime.cpp "+RuntimeFolder+"statemachineI.cpp "+RuntimeFolder+"threadpool.cpp " + RuntimeFolder+"threadpoolmanager.cpp -std=gnu++11";
		}
		
		Shared.writeOutSource(path_, makefileName_, makeFile);
	}

	private  String createEventSource(EList<Element> elements_)
	{
		List<Signal> signalList=new ArrayList<Signal>();
		Shared.getTypedElements(signalList,elements_,UMLPackage.Literals.SIGNAL);
		String forwardDecl="";
		String source = GenerationTemplates.EventBase()+"\n";
		List<Pair<String,String>> allParam=new LinkedList<Pair<String,String>>();
		
		for(Signal item:signalList)
		{
			List<Pair<String,String>> currentParams=getSignalParams(item);
			allParam.addAll(currentParams);
			source+=GenerationTemplates.EventClass(item.getName(),currentParams);
		}
		
		source+=GenerationTemplates.EventClass("InitSignal", new ArrayList<Pair<String,String>>());
		
		for(Pair<String,String> param:allParam)
		{
			if(!Shared.isBasicType(param.getFirst()))
			{
				String tmp=GenerationTemplates.ForwardDeclaration(param.getFirst());
				if(!forwardDecl.contains(tmp))
				{
					forwardDecl+=tmp;	
				}
			}
		}
		forwardDecl+="\n";
		source=forwardDecl+source;
		return GenerationTemplates.EventHeaderGuard(source);
	}
	
	private List<Pair<String,String>> getSignalParams(Signal signal_)
	{
		List<Pair<String,String>> ret = new ArrayList<Pair<String,String>>();
		for(Property prop:signal_.getOwnedAttributes())
		{
			ret.add(new Pair<String, String>(prop.getType().getName(),prop.getName()));
		}
		return ret;
	} 
}
