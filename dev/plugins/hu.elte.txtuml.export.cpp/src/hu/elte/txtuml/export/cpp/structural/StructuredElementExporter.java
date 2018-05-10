package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.AttributeOwner;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.CppExporterUtils.TypeDescriptor;
import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.IDependencyCollector;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates.VariableType;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates;

public abstract class StructuredElementExporter<StructuredElement extends OperationOwner & AttributeOwner> implements ICppCompilationUnit, IDependencyCollector {

	private static final String UKNOWN_TYPE = "!!UNKNOWNTYPE!!";

	protected StructuredElement structuredElement;
	protected String name;
	private String dest;

	protected ActivityExporter activityExporter;
	protected DependencyExporter dependencyExporter;
	private Boolean testing;
	
	public StructuredElementExporter(StructuredElement structuredElement, String name, String dest) {
		this.structuredElement = structuredElement;
		this.name = name;
		this.dest = dest;
		
	}
	
	public void setTesting(Boolean testing) {
		this.testing = testing;
	}

	protected void init() {
		dependencyExporter = new DependencyExporter();
		activityExporter = new ActivityExporter(Optional.of(this));
	}

	protected String createPublicAttributes() {
		return createAttributes(VisibilityKind.PUBLIC_LITERAL);
	}

	protected String createProtectedAttributes() {
		return createAttributes(VisibilityKind.PROTECTED_LITERAL);
	}

	protected String createPrivateAttrbutes() {
		return createAttributes(VisibilityKind.PRIVATE_LITERAL);
	}

	protected String createPublicOperationDeclarations() {
		return createOperationDeclarations(VisibilityKind.PUBLIC_LITERAL);
	}

	protected String createProtectedOperationsDeclarations() {
		return createOperationDeclarations(VisibilityKind.PROTECTED_LITERAL);
	}

	protected String createPrivateOperationsDeclarations() {
		return createOperationDeclarations(VisibilityKind.PRIVATE_LITERAL);
	}

	protected String createOperationDefinitions() {
		StringBuilder source = new StringBuilder("");
		for (Operation operation : structuredElement.getOwnedOperations()) {
			if (!CppExporterUtils.isConstructor(operation)) {
				String returnType = getReturnType(operation.getReturnResult());
				if (!operation.isAbstract()) {
					ActivityExportResult activityResult = activityExporter.createFunctionBody(CppExporterUtils.getOperationActivity(operation));				
					source.append(FunctionTemplates.functionDef(name, returnType, operation.getName(),
							CppExporterUtils.getOperationParams(operation), activityResult.getActivitySource()));
					
				} else {
					assert(testing != null);
					source.append(FunctionTemplates.abstractFunctionDef(name, returnType, operation.getName(),
							CppExporterUtils.getOperationParams(operation),testing));

				}

			}

		}
		return source.toString();
	}

	protected String getReturnType(Parameter returnResult) {
		String returnType = null;
		if (returnResult != null) {
			returnType = returnResult.getType().getName();
		}
		return returnType;
	}

	protected List<TypeDescriptor> getOperationParamTypes(Operation operation) {
		List<TypeDescriptor> ret = new ArrayList<>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					ret.add(new TypeDescriptor(param.getType().getName(), param.getLower(), param.getUpper()));
				}
			}
		}
		return ret;
	}

	protected String operationDecl(Operation op) {
		String returnType = getReturnType(op.getReturnResult());
		if (op.isAbstract()) {
			return FunctionTemplates.functionDecl(returnType, op.getName(), getOperationParamTypes(op),
					GenerationNames.ModifierNames.AbstractModifier, false);
		} else {
			return FunctionTemplates.functionDecl(returnType, op.getName(), getOperationParamTypes(op));
		}

	}

	private String createAttributes(VisibilityKind modifyer) {
		StringBuilder source = new StringBuilder("");
		for (Property attribute : structuredElement.getOwnedAttributes()) {
			if(attribute.getType().eClass().equals(UMLPackage.Literals.INTERFACE)) {
				continue;
			}
			if (attribute.getVisibility().equals(modifyer)) {
				String type = UKNOWN_TYPE;
				if (attribute.getType() != null) {
					type = attribute.getType().getName();
				}

				if (isSimpleAttribute(attribute)) {
					source.append(ObjectDeclDefTemplates.propertyDecl(type, attribute.getName(), attribute.getDefault(), 
							VariableType.getUMLMultpliedElementType(attribute.getLower(), attribute.getUpper())));
				} else {
					dependencyExporter.addDependency(type);
				}
			}
		}
		return source.toString();
	}

	private String createOperationDeclarations(VisibilityKind modifier) {
		StringBuilder source = new StringBuilder("");
		for (Operation operation : structuredElement.getOwnedOperations()) {
			if (operation.getVisibility().equals(modifier)) {
				String returnType = getReturnType(operation.getReturnResult());
				if (!CppExporterUtils.isConstructor(operation)) {
					source.append(operationDecl(operation));
				}
				if (returnType != null) {
					dependencyExporter.addDependency(returnType);
				}
				dependencyExporter.addDependencies(getOperationParamTypes(operation)
						.stream().map(t -> t.getTypeName()).collect(Collectors.toList()));
			}
		}

		return source.toString();
	}

	private boolean isSimpleAttribute(Property property) {
		return property.getAssociation() == null;
	}
	
	
	// Dependecy owner part
	@Override
	public void addDependency(String dependency) {
		dependencyExporter.addDependency(dependency);
	}
	
	@Override
	public void addCppOnlyDependency(String dependency) {
		dependencyExporter.addCppOnlyDependency(dependency);
	}
	
	@Override
	public void addHeaderOnlyIncludeDependency(String type) {
		dependencyExporter.addHeaderOnlyIncludeDependency(type);
		
	}
	
	@Override
	public String getUnitName() {
		return name;
	}
	
	@Override
	public String getDestination() {
		return dest;
	}

}
