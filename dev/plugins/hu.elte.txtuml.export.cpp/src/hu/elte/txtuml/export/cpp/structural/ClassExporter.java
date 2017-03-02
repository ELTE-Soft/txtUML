package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.statemachine.StateMachineExporter;
import hu.elte.txtuml.export.cpp.statemachine.SubStateMachineExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ConstructorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;

public class ClassExporter extends StructuredElementExporter<Class> {

	private List<String> subMachines;
	private List<String> additionalSourcesNames;
	private AssociationExporter associationExporter;
	private ConstructorExporter constructorExporter;

	private Shared shared;

	private StateMachineExporter stateMachineExporter;
	private PortExporter portExporter;
	private SubStateMachineExporter subStateMachineExporter;

	private int poolId;

	public ClassExporter(Shared shared) {
		this.shared = shared;
	}
	
	

	public List<String> getAdditionalSources() {
		return additionalSourcesNames;
	}

	@Override
	public void exportStructuredElement(Class structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException {
		super.init(shared);
		super.setStructuredElement(structuredElement);
		constructorExporter = new ConstructorExporter(shared,structuredElement.getOwnedOperations());
		associationExporter = new AssociationExporter();
		stateMachineExporter = new StateMachineExporter(shared);
		additionalSourcesNames = new ArrayList<String>();
		subMachines = new LinkedList<String>();
		portExporter = new PortExporter();


		stateMachineExporter.setName(name);
		stateMachineExporter.setStateMachineThreadPoolId(poolId);

		createSource(sourceDestination);

	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

	public List<String> getSubmachines() {
		return stateMachineExporter.getSubMachineNameList();
	}

	private void createSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		associationExporter.exportAssociations(structuredElement.getOwnedAttributes());
		stateMachineExporter.createStateMachineRegion(structuredElement);
		if (stateMachineExporter.ownStateMachine()) {
			stateMachineExporter.createMachine();

			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				subStateMachineExporter = new SubStateMachineExporter(shared);
				subStateMachineExporter.setRegion(entry.getValue().getSecond());
				subStateMachineExporter.setName(entry.getValue().getFirst());
				subStateMachineExporter.setParentClass(name);
				subStateMachineExporter.createSubSmSource(dest);
				subMachines.addAll(subStateMachineExporter.getSubMachineNameList());
			}
		}

		source = createClassHeaderSource();
		String externalDeclerations = associationExporter.createLinkFunctionDeclarations(name);
		Shared.writeOutSource(dest, GenerationTemplates.headerName(name),
				Shared.format(HeaderTemplates.headerGuard(source + externalDeclerations, name)));

		source = createClassCppSource();
		Shared.writeOutSource(dest, GenerationTemplates.sourceName(name),
				Shared.format(getAllDependencies(false) + source));
	}
	
	public boolean isStateMachineOwner() {
		return stateMachineExporter.ownStateMachine();
		
		
	}

	private String createClassHeaderSource() {
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

		if (stateMachineExporter.ownStateMachine()) {

			publicParts.append(stateMachineExporter.createStateEnumCode());
			publicParts.append(portExporter.createPortEnumCode(structuredElement.getOwnedPorts()));
			privateParts.append(stateMachineExporter.createStateMachineRelatedHeadedDeclarationCodes());

			if (stateMachineExporter.ownSubMachine()) {
				source = HeaderTemplates
						.simpleStateMachineClassHeader(getAllDependencies(true), name, getBaseClass(), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), true)
						.toString();
			} else {
				source = HeaderTemplates.hierarchicalStateMachineClassHeader(getAllDependencies(true), name,
						getBaseClass(), getSubmachines(), publicParts.toString(), protectedParts.toString(),
						privateParts.toString(), true);
			}
		} else {
			source = HeaderTemplates.classHeader(getAllDependencies(true), name, getBaseClass(), publicParts.toString(),
					protectedParts.toString(), privateParts.toString(), true);
		}
		return source;
	}

	private String createClassCppSource() {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		shared.getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE, structuredElement.allOwnedElements());
		if (stateMachineExporter.ownStateMachine()) {
			source.append(stateMachineExporter.createStateMachineRelatedCppSourceCodes());

		}

		source.append(super.createOperationDefinitions());
		source.append(constructorExporter.exportConstructorsDefinitions(name, stateMachineExporter.ownStateMachine()));
		source.append(stateMachineExporter.ownStateMachine() ? ConstructorTemplates.destructorDef(name, true)
				: ConstructorTemplates.destructorDef(name, false));

		return source.toString();
	}

	private String getAllDependencies(Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		dependencyExporter.addDependencies(associationExporter.getAssociatedPropertyTypes());

		if (stateMachineExporter.ownStateMachine()) {
			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				dependencyExporter.addDependecy(entry.getValue().getFirst());
			}

		}

		if (getBaseClass() != null) {
			source.append(PrivateFunctionalTemplates.include(getBaseClass()));
		}

		if (!isHeader) {
			source.append(dependencyExporter.createDependencyCppIncludeCode(name));
			if (stateMachineExporter.ownStateMachine()) {
				source.append(PrivateFunctionalTemplates.include(GenerationTemplates.DeploymentHeader));
				source.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
			}
			source.append(PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName));
			if (associationExporter.ownAssociation()) {
				source.append(PrivateFunctionalTemplates.include(LinkTemplates.AssociationsStructuresHreaderName));

			}
			// TODO analyze what dependency is necessary..
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.ActionPath));
			source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + GenerationTemplates.StandardFunctionsHeader));
			source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + GenerationTemplates.TimerInterfaceHeader));
			source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + GenerationTemplates.TimerHeader));
		} else {
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.TypesFilePath));
			source.append(dependencyExporter.createDependencyHeaderIncludeCode());
			if (associationExporter.ownAssociation()) {
				source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + LinkTemplates.AssocationHeader));
			}

		}

		source.append("\n");
		return source.toString();
	}

	private String getBaseClass() {
		if (!structuredElement.getGeneralizations().isEmpty()) {
			return structuredElement.getGeneralizations().get(0).getGeneral().getName();
		} else {
			return null;
		}
	}

}
