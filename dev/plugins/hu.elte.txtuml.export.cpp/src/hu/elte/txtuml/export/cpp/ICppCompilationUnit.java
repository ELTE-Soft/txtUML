package hu.elte.txtuml.export.cpp;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;

public interface ICppCompilationUnit {

	
	enum UnitType {
		Cpp,
		Header
	}
	
	String getUnitName();
	String getUnitNamespace();
	String createUnitCppCode();
	String createUnitHeaderCode();
	String getUnitDependencies(UnitType type);
	String getDesniation();
	
	default void createAddtionoalSources() throws FileNotFoundException, UnsupportedEncodingException {}
	
	default void createUnitSource() throws FileNotFoundException, UnsupportedEncodingException {
		createAddtionoalSources();
		
		String headerSource = createUnitHeaderCode();
		String cppSource = createUnitCppCode();
		
		
		headerSource = getUnitDependencies(UnitType.Header) + GenerationTemplates.putNamespace(headerSource, getUnitNamespace());
		cppSource = getUnitDependencies(UnitType.Cpp) + GenerationTemplates.putNamespace(cppSource, getUnitNamespace());;
		
		CppExporterUtils.writeOutSource(getDesniation(), GenerationTemplates.headerName(getUnitName()),
				CppExporterUtils.format(HeaderTemplates.headerGuard(headerSource, getUnitName())));
		
		
		CppExporterUtils.writeOutSource(getDesniation(), GenerationTemplates.sourceName(getUnitName()),
				CppExporterUtils.format(cppSource));	
		
		
	}
	 

}
