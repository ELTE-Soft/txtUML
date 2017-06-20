package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.uml2.uml.AttributeOwner;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates;

public abstract class StructuredElementExporter<StructuredElement extends OperationOwner & AttributeOwner> {

	private static final String UKNOWN_TYPE = "!!UNKNOWNTYPE!!";

	protected StructuredElement structuredElement;
	protected String name;

	protected ActivityExporter activityExporter;
	protected DependencyExporter dependencyExporter;

	protected StructuredElementExporter() {
	}

	public void setName(String name) {
		this.name = name;
	}

	abstract public void exportStructuredElement(StructuredElement structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException;

	protected void setStructuredElement(StructuredElement structuredElement) {
		this.structuredElement = structuredElement;
	}

	public void init() {
		dependencyExporter = new DependencyExporter();
		activityExporter = new ActivityExporter();
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
			String funcBody = activityExporter.createFunctionBody(CppExporterUtils.getOperationActivity(operation));
			dependencyExporter.addDependencies(activityExporter.getAdditionalClassDependencies());

			if (!CppExporterUtils.isConstructor(operation)) {

				String returnType = getReturnType(operation.getReturnResult());
				source.append(FunctionTemplates.functionDef(name, returnType, operation.getName(),
						CppExporterUtils.getOperationParams(operation), funcBody));
			}

		}
		return source.toString();
	}

	private String createAttributes(VisibilityKind modifyer) {
		StringBuilder source = new StringBuilder("");
		for (Property attribute : structuredElement.getOwnedAttributes()) {
			if (attribute.getVisibility().equals(modifyer)) {
				String type = UKNOWN_TYPE;
				if (attribute.getType() != null) {
					type = attribute.getType().getName();
				}

				
				if (isSimpleAttribute(attribute)) {

					source.append(ObjectDeclDefTemplates.propertyDecl(type, attribute.getName(), attribute.getDefault()));
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
					source.append(FunctionTemplates.functionDecl(returnType, operation.getName(),
							getOperationParamTypes(operation)));
				}
				if (returnType != null) {
					dependencyExporter.addDependency(returnType);
				}
				dependencyExporter.addDependencies(getOperationParamTypes(operation));
			}
		}

		return source.toString();
	}

	private String getReturnType(Parameter returnResult) {
		String returnType = null;
		if (returnResult != null) {
			returnType = returnResult.getType().getName();
		}
		return returnType;
	}

	private List<String> getOperationParamTypes(Operation operation) {
		List<String> ret = new ArrayList<String>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					ret.add(param.getType().getName());
				}
			}
		}
		return ret;
	}

	private boolean isSimpleAttribute(Property property) {
		return property.getAssociation() == null;
	}

}
