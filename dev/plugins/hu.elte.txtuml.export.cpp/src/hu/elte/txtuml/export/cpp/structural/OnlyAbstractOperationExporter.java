package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;

public class OnlyAbstractOperationExporter extends StructuredElementExporter<Class> {
	public OnlyAbstractOperationExporter() {
		super(o -> o.isAbstract());
	}

	@Override
	public void exportStructuredElement(Class structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException {
		super.init();
		super.setStructuredElement(structuredElement);
		String publicAbstractOperationDecl = super.createPublicOperationDeclarations();
		String privateAbstractOperationDecl = super.createPrivateOperationsDeclarations();
		String protectedAbstractOperationDecl = super.createProtectedOperationsDeclarations();

		String dependency = dependencyExporter.createDependencyHeaderIncludeCode()
				+ PrivateFunctionalTemplates.include(GenerationNames.FileNames.TypesFilePath);

		String source = HeaderTemplates.classHeader(dependency, name, null, publicAbstractOperationDecl,
				protectedAbstractOperationDecl, privateAbstractOperationDecl);

		CppExporterUtils.writeOutSource(sourceDestination, GenerationTemplates.headerName(name),
				HeaderTemplates.headerGuard(source, name));

	}

	@Override
	protected String operationDecl(Operation op) {
		String returnType = getReturnType(op.getReturnResult());
		return FunctionTemplates.functionDecl(returnType, op.getName(), getOperationParamTypes(op),
				GenerationNames.ModifierNames.AbstractModifier, true);

	}

}
