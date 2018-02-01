package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.statemachine.StateMachineExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ConstructorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates.HeaderInfo;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;
import hu.elte.txtuml.utils.Pair;

public class ClassExporter extends StructuredElementExporter<Class> {

	private List<String> additionalSourcesNames;
	private List<String> baseClasses;
	private List<String> interfacesToImplement;
	private AssociationExporter associationExporter;
	private ConstructorExporter constructorExporter;

	private StateMachineExporter stateMachineExporter;
	private PortExporter portExporter;
	private String abstractInterface;

	private int poolId;

	public ClassExporter(Class structuredElement, String name, String sourceDestination) {
		super(structuredElement, name, sourceDestination);
		super.init();
		baseClasses = new LinkedList<String>();
		interfacesToImplement = new LinkedList<String>();
		constructorExporter = new ConstructorExporter(structuredElement.getOwnedOperations(), super.activityExporter);
		associationExporter = new AssociationExporter();
		additionalSourcesNames = new ArrayList<String>();
		baseClasses.clear();
		interfacesToImplement.clear();
		portExporter = new PortExporter();

		StateMachine classSM = CppExporterUtils.getStateMachine(structuredElement);
		if (classSM != null) {

			stateMachineExporter = new StateMachineExporter(classSM, this, poolId);
		}
		
	}

	public List<String> getAdditionalSources() {
		return additionalSourcesNames;
	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

	public List<String> getSubmachines() {
		assert(stateMachineExporter != null);
		if(stateMachineExporter != null) {
			return stateMachineExporter.getAllSubmachineName();
		} else {
			return Collections.emptyList();
		}
		
	}

	public void setAbstractInterface(String abstractInterface) {
		this.abstractInterface = abstractInterface;
	}

	public void removeAbstractInterface() {
		this.abstractInterface = null;
	}


	private void collectModelBaseClasses() {
		for (Generalization base : structuredElement.getGeneralizations()) {
			baseClasses.add(base.getGeneral().getName());
		}

		if (abstractInterface != null && !baseClasses.contains(abstractInterface)) {
			interfacesToImplement.add(abstractInterface);
		}

	}

	@Override
	public String getUnitNamespace() {
		return GenerationNames.Namespaces.ModelNamespace;
	}

	@Override
	public String createUnitCppCode() {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		CppExporterUtils.getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE,
				structuredElement.allOwnedElements());
		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
			source.append(stateMachineExporter.createStateMachineRelatedCppSourceCodes());

		}

		source.append(super.createOperationDefinitions());
		source.append(constructorExporter.exportConstructorsDefinitions(name, CppExporterUtils.isStateMachineOwner(structuredElement)));
		source.append(CppExporterUtils.isStateMachineOwner(structuredElement) ? ConstructorTemplates.destructorDef(name, true)
				: ConstructorTemplates.destructorDef(name, false));

		return source.toString();
	}

	@Override
	public String createUnitHeaderCode() {
		String source = "";
		StringBuilder privateParts = new StringBuilder(
				super.createPrivateAttrbutes() + super.createPrivateOperationsDeclarations());
		StringBuilder protectedParts = new StringBuilder(
				super.createProtectedAttributes() + super.createProtectedOperationsDeclarations());
		StringBuilder publicParts = new StringBuilder(
				super.createPublicAttributes() + super.createPublicOperationDeclarations());

		publicParts.append(constructorExporter.exportConstructorDeclarations(name));
		publicParts.append(ConstructorTemplates.destructorDecl(name));

		publicParts.append("\n" + associationExporter.createAssociationMemberDeclarationsCode());

		publicParts.append(LinkTemplates.templateLinkFunctionGeneralDef(LinkTemplates.LinkFunctionType.Link));
		publicParts.append(LinkTemplates.templateLinkFunctionGeneralDef(LinkTemplates.LinkFunctionType.Unlink));
		
		publicParts.append(portExporter.createPortEnumCode(structuredElement.getOwnedPorts()));

		collectModelBaseClasses();
		//TODO dependency miert kell oda?
		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {

			publicParts.append(stateMachineExporter.createStateEnumCode());
			privateParts.append(stateMachineExporter.createStateMachineRelatedHeadedDeclarationCodes());
			source = HeaderTemplates
						.classHeader("", baseClasses, interfacesToImplement,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), 
								new HeaderInfo(name,
										new HeaderTemplates.StateMachineClassHeaderType(stateMachineExporter.ownSubMachine() ? 
												Optional.of(getSubmachines()) : Optional.empty()))
							);
		} else {
			source = HeaderTemplates
					.classHeader("", baseClasses, interfacesToImplement, publicParts.toString(),
					protectedParts.toString(), privateParts.toString(), 
					new HeaderInfo(name,new HeaderTemplates.SimpleClassHeaderType())	);
		}
		return source;
	}

	@Override
	public String getUnitDependencies(UnitType type) {
		StringBuilder source = new StringBuilder("");
		dependencyExporter.addDependencies(associationExporter.getAssociatedPropertyTypes());
		for (String baseClassName : baseClasses) {
			source.append(PrivateFunctionalTemplates.include(baseClassName));
		}
		
		for (String pureInterfaceName : interfacesToImplement) {
			source.append(PrivateFunctionalTemplates.include(pureInterfaceName));
		}

		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				dependencyExporter.addDependency(entry.getValue().getFirst());
			}
			
			dependencyExporter.addHeaderOnlyDependencies(stateMachineExporter.getAllSubmachineName());

		}

		if (type == UnitType.Cpp) {
			source.append(dependencyExporter.createDependencyCppIncludeCode(name));
			if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
				source.append(PrivateFunctionalTemplates.include(GenerationTemplates.DeploymentHeader));
				source.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
			}
			source.append(PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName));
			if (associationExporter.ownAssociation()) {
				source.append(PrivateFunctionalTemplates.include(LinkTemplates.AssociationsStructuresHreaderName));

			}
			// TODO analyze what dependency is necessary..
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.ActionPath));
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.StringUtilsPath));
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.CollectionUtilsPath));
		} else if(type == UnitType.Header) {
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.TypesFilePath));
			if (associationExporter.ownAssociation()) {
				source.append(
						PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + LinkTemplates.AssocationHeader));
			}
			source.append(GenerationTemplates.putNamespace(dependencyExporter.createDependencyHeaderIncludeCode(),
					GenerationNames.Namespaces.ModelNamespace));

		}

		source.append("\n");
		return source.toString();
	}

}
