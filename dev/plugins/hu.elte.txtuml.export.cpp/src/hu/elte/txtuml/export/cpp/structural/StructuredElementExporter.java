package hu.elte.txtuml.export.cpp.structural;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.uml2.uml.AttributeOwner;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class StructuredElementExporter<StructuredElement extends OperationOwner & AttributeOwner> {
	
	private static final String UKNOWN_TYPE = "!!UNKNOWNTYPE!!";

	protected StructuredElement structuredElement;
	protected String name;
	protected Set<String> dependecies;
	
	protected ActivityExporter activityExporter;
	
	
	protected StructuredElementExporter() {}
	protected StructuredElementExporter(StructuredElement structuredElement, String name) {
		init(structuredElement, name);
	}

	protected void init(StructuredElement structuredElement, String name) {
		dependecies = new HashSet<String>();
		activityExporter = new ActivityExporter();
		
		this.structuredElement = structuredElement;
		this.name = name;
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
	
	protected String createPublicOperationDecelerations() {
		return createOperationDecelerations(VisibilityKind.PUBLIC_LITERAL);
	}
	
	protected String createProtectedOperationsDeclerations() {
		return createOperationDecelerations(VisibilityKind.PROTECTED_LITERAL);
	}
	
	protected String createPrivateOperationsDeclerations() {
		return createOperationDecelerations(VisibilityKind.PRIVATE_LITERAL);
	}
	
	protected String createOperationDefinitions() {
		StringBuilder source = new StringBuilder("");
		for (Operation operation : structuredElement.getOwnedOperations()) {
			activityExporter.init();
			String funcBody = activityExporter.createfunctionBody(Shared.getOperationActivity(operation));
			if (!Shared.isConstructor(operation)) {

				String returnType = getReturnType(operation.getReturnResult());
				source.append(GenerationTemplates.functionDef(name, returnType, operation.getName(),
						Shared.getOperationParams(operation), funcBody));
			}		
			

		}
		return source.toString();
	}
	
	protected String createDependencyIncudesCode(boolean isHeader) {
		StringBuilder includes = new StringBuilder("");
		for(String type : dependecies) {
			if(isHeader) {
				includes.append(GenerationTemplates.forwardDeclaration(type));
			}
			else {
				includes.append(GenerationTemplates.cppInclude(type));
			}
		}
		
		return includes.toString();
	}

	private String createAttributes(VisibilityKind modifyer) {
		StringBuilder source = new StringBuilder("");
		for (Property attribute : structuredElement.getOwnedAttributes()) {
			if (attribute.getVisibility().equals(modifyer)) {
				String type = UKNOWN_TYPE;
				if (attribute.getType() != null) {
					type = attribute.getType().getName();
				}

				if (!Shared.isBasicType(type)) {
					dependecies.add(type);
				}
				if (isSimpleAttribute(attribute)) {
					source.append(GenerationTemplates.variableDecl(type, attribute.getName(), 1));
				}
			}
		}
		return source.toString();
	}

	private String createOperationDecelerations(VisibilityKind modifyer) {
		StringBuilder source = new StringBuilder("");
		for (Operation operation : structuredElement.getOwnedOperations()) {
			if (operation.getVisibility().equals(modifyer)) {
				String returnType = getReturnType(operation.getReturnResult());
				if(!Shared.isConstructor(operation)) {
					source.append(GenerationTemplates.functionDecl(returnType, operation.getName(), getOperationParamTypes(operation)));
				}
				if(returnType != null) {
					dependecies.add(returnType);
				}
				dependecies.addAll(getOperationParamTypes(operation));
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
