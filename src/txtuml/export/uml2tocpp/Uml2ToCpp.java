package txtuml.export.uml2tocpp;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 * upcoming features:
 * 	TODO -logging
 *  TODO -C++ debug log in transition actions
 *  TODO - ?exceptions, depending on design 
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
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLPackage;

import txtuml.export.uml2tocpp.templates.GenerationTemplates;

public class Uml2ToCpp
{
	private static final String DefaultCompiler="g++";
	private static final String CppFileLocation="src"+File.separator+"txtuml"+File.separator+"export"+File.separator+"uml2tocpp"+File.separator+"cppfiles"+File.separator;
	private static final String RuntimeFolder=GenerationTemplates.RuntimePath;
	private static final String RuntimeCppFileLocation=CppFileLocation+RuntimeFolder;
	private static final String RuntimeLibName="libsmrt.a";
	private static final String DefaultMakeFileName="projectmakefile";
	
	public static void main(String[] args) 
	{	
		if(args.length < 2) {
			System.out.println("Two command line arguments needed:input file, output directory");
			return;
		}
		
		try
		{
			Model model= Util.loadModel(args[0]);
			buildCppCode(model,args);
		}
		catch(Exception e)
		{
			System.out.println("UnexceptedException!");
		}
	}
	
	public static void buildCppCode(Model model_,String[] args) 
	{
		System.out.println("Compile started on: "+args[0]);
		Boolean rt=false;
		for(String flag:args)
		{
			if(flag.equals("-rt"))
			{
				rt=true;
			}
		}

		try {
			EList<Element> elements=model_.allOwnedElements();
			
			String source=createEventSource(elements,rt);
			
			Shared.writeOutSource(args[1],(GenerationTemplates.EventHeader),source);
			
			copyPreWrittenCppFiles(args[1],rt);

			List<Class> classList=new ArrayList<Class>();
			List<String> ClassNames=new LinkedList<String>();
			Shared.getTypedElements(classList,elements,UMLPackage.Literals.CLASS);
			for(Class item:classList)
			{
				ClassExporter classExporter=new ClassExporter();
				classExporter.createSource(item, args[1], rt);
				
				ClassNames.addAll(classExporter.getSubmachines());
				ClassNames.add(item.getName());
			}
			
			createMakeFile(args[1],DefaultMakeFileName,ClassNames,rt);
			
			System.out.println("Compile completed");
		} catch(IOException ioe) 
		{
			System.out.println("IO error.");
        }
	}

	//TODO get each file from a path an copy all, not like this ...
	private static void copyPreWrittenCppFiles(String destination_,Boolean rt_) throws IOException 
	{
		try
		{
			Files.createDirectory(Paths.get(destination_));
		}
		catch(IOException e){}
		
		Files.copy(Paths.get(CppFileLocation+GenerationTemplates.StateMachineBaseHeader),
				Paths.get(destination_+GenerationTemplates.StateMachineBaseHeader), StandardCopyOption.REPLACE_EXISTING);
		if(rt_)
		{
			try
			{
				Files.createDirectory(Paths.get(destination_+RuntimeFolder));
			}
			catch(IOException e){}
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtimemain.cpp"),
					Paths.get(destination_+"main.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"eventI.hpp"),
					Paths.get(destination_+RuntimeFolder+"eventI.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtime.hpp"),
					Paths.get(destination_+RuntimeFolder+"runtime.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtime.cpp"),
					Paths.get(destination_+RuntimeFolder+"runtime.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"runtimetypes.hpp"),
					Paths.get(destination_+RuntimeFolder+"runtimetypes.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"statemachineI.hpp"),
					Paths.get(destination_+RuntimeFolder+"statemachineI.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"statemachineI.cpp"),
					Paths.get(destination_+RuntimeFolder+"statemachineI.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"threadpool.hpp"),
					Paths.get(destination_+RuntimeFolder+"threadpool.hpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"threadpool.cpp"),
					Paths.get(destination_+RuntimeFolder+"threadpool.cpp"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(RuntimeCppFileLocation+"threadsafequeue.hpp"),
					Paths.get(destination_+RuntimeFolder+"threadsafequeue.hpp"), StandardCopyOption.REPLACE_EXISTING);
		}
		else
		{
			Files.copy(Paths.get(CppFileLocation+"main.cpp"),
					Paths.get(destination_+"main.cpp"), StandardCopyOption.REPLACE_EXISTING);
		}
		
	}

	private static void createMakeFile(String path_,String outputName_,List<String> classNames_,Boolean rt_) throws FileNotFoundException, UnsupportedEncodingException
	{
		String makeFile="CC="+DefaultCompiler+"\n\n";
		
		if(rt_)
		{
			makeFile+="runtime: "+RuntimeFolder+"runtime.cpp "+RuntimeFolder+"statemachineI.cpp "+RuntimeFolder+"threadpool.cpp\n"+
					"\t$(CC) -c "+RuntimeFolder+"runtime.cpp "+RuntimeFolder+"statemachineI.cpp "+RuntimeFolder+"threadpool.cpp -std=c++11 -I.\n\n"+
					"ar rcs "+RuntimeLibName+" runtime.o statemachineI.o threadpool.o.\n\n"
					;
		}
		
		makeFile+=outputName_+":";
		String fileList=" main.cpp";
		for(String file:classNames_)
		{
			fileList+=" "+GenerationTemplates.SourceName(file);
		}
		makeFile+=fileList+"\n";
		makeFile+="\t$(CC) -o "+outputName_+fileList+" -std=c++11 -I";
		
		if(rt_)
		{
			makeFile+=" "+RuntimeFolder+" -LC "+RuntimeLibName;
		}
		
		Shared.writeOutSource(path_, outputName_+".makefile", makeFile+".");
	}

	private static String createEventSource(EList<Element> elements_,Boolean rt_)
	{
		List<Signal> signalList=new ArrayList<Signal>();
		Shared.getTypedElements(signalList,elements_,UMLPackage.Literals.SIGNAL);
		String forwardDecl="";
		String source = GenerationTemplates.EventBase(rt_)+"\n";
		List<Util.Pair<String,String>> allParam=new LinkedList<Util.Pair<String,String>>();
		
		for(Signal item:signalList)
		{
			List<Util.Pair<String,String>> currentParams=getSignalParams(item);
			allParam.addAll(currentParams);
			source+=GenerationTemplates.EventClass(item.getName(),currentParams,rt_);
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
