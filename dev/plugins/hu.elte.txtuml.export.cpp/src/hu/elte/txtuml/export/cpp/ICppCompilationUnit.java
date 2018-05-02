package hu.elte.txtuml.export.cpp;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;

public interface ICppCompilationUnit {

	enum UnitType {
		Cpp, Header
	}

	String getUnitName();

	String getUnitNamespace();

	String createUnitCppCode() throws FileNotFoundException, UnsupportedEncodingException;

	String createUnitHeaderCode();

	String getUnitDependencies(UnitType type);

	String getDestination();

	default void createAdditionalSources() throws FileNotFoundException, UnsupportedEncodingException {
	}

	default void createUnitSource() throws FileNotFoundException, UnsupportedEncodingException {
		createAdditionalSources();

		String headerSource = createUnitHeaderCode();
		String cppSource = createUnitCppCode();

		headerSource = getUnitDependencies(UnitType.Header)
				+ GenerationTemplates.putNamespace(headerSource, getUnitNamespace());
		cppSource = getUnitDependencies(UnitType.Cpp) + GenerationTemplates.putNamespace(cppSource, getUnitNamespace());
		;

		CppExporterUtils.writeOutSource(getDestination(), GenerationTemplates.headerName(getUnitName()),
				CppExporterUtils.format(HeaderTemplates.headerGuard(headerSource, getUnitName())));

		CppExporterUtils.writeOutSource(getDestination(), GenerationTemplates.sourceName(getUnitName()),
				CppExporterUtils.format(cppSource));

	}

}
