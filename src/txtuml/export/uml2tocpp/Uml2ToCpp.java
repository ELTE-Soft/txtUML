package txtuml.export.uml2tocpp;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 * upcoming features:
 * 	TODO -logging
 *  TODO - ?exceptions, depending on design 
 *  TODO -redesign dependency analysis and include generation
 * 	TODO -refactoring more
 **********************************************************/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLPackage;

import txtuml.export.uml2tocpp.templates.GenerationTemplates;
import txtuml.export.uml2tocpp.templates.Options;

public class Uml2ToCpp
{
	private static final String DefaultCompiler="g++";
	private static final String GCCDebugSymbolOn="-D_DEBUG";//the symbol located in GenerationNames
	private static final String CppFileLocation="src"+File.separator+"txtuml"+File.separator+"export"+File.separator+"uml2tocpp"+File.separator+"cppfiles"+File.separator;
	private static final String RuntimeFolder=GenerationTemplates.RuntimePath;
	private static final String RuntimeCppFileLocation=CppFileLocation+RuntimeFolder;
	private static final String RuntimeLibName="libsmrt.a";
	private static final String DefaultMakeFileName="make";
	private static final String DefaultModelName="main";
	
	
	//Options
	private static final String RuntimeOption="-rt";
	private static final String HelpAOption="-h";
	private static final String HelpBOption="-help";
	private static final String DebugLogOption="-cdl";
	
	public static void main(String[] args) 
	{	
		
		if(args[0].equals(HelpAOption) || args[0].equals(HelpBOption))
		{
			help();
			return;
		}
		
		if(args.length < 2 ) 
		{
			System.out.println("Missing arguments for more information use -h or -help");
			return;
		}

		
		try
		{
			Model model= Util.loadModel(args[0]);
			buildCppCode(model,args);
		}
		catch(WrappedException e)
		{
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.println("\nUnexceptedException!");
		}
	}
	
	public static void buildCppCode(Model model_,String[] args) 
	{
		System.out.println("Compile started on: "+args[0]);
		for(String flag:args)
		{
			if(flag.equals(RuntimeOption))
			{
				Options.setRuntime();
			}
			else if(flag.equals(DebugLogOption))
			{
				Options.setDebugLog();
			}
		}

		try {
			EList<Element> elements=model_.allOwnedElements();
			
			String source=createEventSource(elements);
			
			Shared.writeOutSource(args[1],(GenerationTemplates.EventHeader),source);
			
			copyPreWrittenCppFiles(args[1]);

			List<Class> classList=new ArrayList<Class>();
			List<String> ClassNames=new LinkedList<String>();
			Shared.getTypedElements(classList,elements,UMLPackage.Literals.CLASS);
			for(Class item:classList)
			{
				ClassExporter classExporter=new ClassExporter();
				classExporter.createSource(item, args[1]);
				
				ClassNames.addAll(classExporter.getSubmachines());
				ClassNames.add(item.getName());
			}
			
			createMakeFile(args[1],DefaultModelName,DefaultMakeFileName,ClassNames);
			
			System.out.println("Compile completed");
		} catch(IOException e) 
		{
			System.out.println("\nCan not write out generated source the problem:");
			System.out.println(e.getMessage());
        }
	}
	
	
	private static void help()
	{
		System.out.println("Usage: UmlToCpp umlModelFilePath outputDirectoryPath [options] ...\n"+
							helpArgs()+
							HelpAOption+","+HelpBOption+"\tPrint this message and exit.\n");
	}
	
	public static String helpArgs()
	{
		return  "C++ Compile Options:\n"+
				RuntimeOption+"\t\tGenerate source with runtime.\n"+
				DebugLogOption+"\t\tGenerate source with log messages in transition actions if _DEBUG is defined.\n";
	}
	

	//TODO get each file from a path an copy all, not like this ...
	private static void copyPreWrittenCppFiles(String destination_) throws IOException 
	{
		try
		{
			Files.createDirectory(Paths.get(destination_));
		}
		catch(IOException e){}
		
		Files.copy(Paths.get(CppFileLocation+GenerationTemplates.StateMachineBaseHeader),
				Paths.get(destination_+File.separator+GenerationTemplates.StateMachineBaseHeader), StandardCopyOption.REPLACE_EXISTING);
		if(Options.Runtime())
		{
			try
			{
				Files.createDirectory(Paths.get(destination_+RuntimeFolder));
			}
			catch(IOException e){}
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtimemain.cpp"),
					Paths.get(destination_+File.separator+"main.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"eventI.hpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"eventI.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtime.hpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"runtime.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtime.cpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"runtime.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtimetypes.hpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"runtimetypes.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"statemachineI.hpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"statemachineI.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"statemachineI.cpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"statemachineI.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"threadpool.hpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"threadpool.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"threadpool.cpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"threadpool.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"threadsafequeue.hpp"),
					Paths.get(destination_+File.separator+RuntimeFolder+"threadsafequeue.hpp"), StandardCopyOption.REPLACE_EXISTING);
		}
		else
		{
			Files.copy(Paths.get(CppFileLocation+"main.cpp"),
					Paths.get(destination_+File.separator+"main.cpp"), StandardCopyOption.REPLACE_EXISTING);
		}
		
	}

	private static void createMakeFile(String path_,String outputName_,String makefileName_,List<String> classNames_) throws FileNotFoundException, UnsupportedEncodingException
	{
		String makeFile="CC="+DefaultCompiler+"\n\nall: "+outputName_+"\n\n";
		
		makeFile+=outputName_+":";
		if(Options.Runtime())
		{
			makeFile+=" "+RuntimeLibName;
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
		makeFile+=" -Wall -o "+outputName_+fileList+" -std=c++11";

		if(Options.Runtime())
		{
			makeFile+=" -I "+RuntimeFolder+" -LC "+RuntimeLibName+"\n\n"+RuntimeLibName+": runtime runtime.o statemachineI.o threadpool.o\n"+
					"\tar rcs "+RuntimeLibName+" runtime.o statemachineI.o threadpool.o\n\n"+
					".PHONY:runtime\n"+
					"runtime:\n\t$(CC) -Wall -c "+RuntimeFolder+"runtime.cpp "+RuntimeFolder+"statemachineI.cpp "+RuntimeFolder+"threadpool.cpp -std=c++11";
		}
		
		Shared.writeOutSource(path_, makefileName_+".makefile", makeFile);
	}

	private static String createEventSource(EList<Element> elements_)
	{
		List<Signal> signalList=new ArrayList<Signal>();
		Shared.getTypedElements(signalList,elements_,UMLPackage.Literals.SIGNAL);
		String forwardDecl="";
		String source = GenerationTemplates.EventBase()+"\n";
		List<Util.Pair<String,String>> allParam=new LinkedList<Util.Pair<String,String>>();
		
		for(Signal item:signalList)
		{
			List<Util.Pair<String,String>> currentParams=getSignalParams(item);
			allParam.addAll(currentParams);
			source+=GenerationTemplates.EventClass(item.getName(),currentParams);
		}
		
		for(Util.Pair<String,String> param:allParam)
		{
			if(!Shared.isBasicType(param.getKey()))
			{
				String tmp=GenerationTemplates.ForwardDeclaration(param.getKey());
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
	
	private static List<Util.Pair<String,String>> getSignalParams(Signal signal_)
	{
		List<Util.Pair<String,String>> ret = new ArrayList<Util.Pair<String,String>>();
		for(Property prop:signal_.getOwnedAttributes())
		{
			ret.add(new Util.Pair<String, String>(prop.getType().getName(),prop.getName()));
		}
		return ret;
	} 
}
