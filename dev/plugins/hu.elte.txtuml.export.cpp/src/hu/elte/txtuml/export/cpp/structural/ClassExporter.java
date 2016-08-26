package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class ClassExporter {
	// private static final int _UMLMany = -1;
	
	private Class cls;

	private Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>
	private List<String> subSubMachines;
	private List<String> additionalSourcesNames;
	private boolean ownConstructor;

	private ActivityExporter activityExporter;
	private AssociationExporter associationExporter;

	private StateMachineExporter stateMachineExporter;
	private SubStateMachineExporter subStateMachineExporter;

	private String name;

	public ClassExporter() {}

	public ClassExporter(Class cls, String name, int poolId) {
		init(cls, name, poolId);
	}

	public void init(Class cls, String name, int poolId) {
		this.cls = cls;
		this.name = name;
		activityExporter = new ActivityExporter();
		associationExporter = new AssociationExporter();
		stateMachineExporter = new StateMachineExporter(name, poolId, cls);

		additionalSourcesNames = new ArrayList<String>();
		subSubMachines = new LinkedList<String>();
		ownConstructor = false;

	}

	public List<String> getAdditionalSources() {
		return additionalSourcesNames;
	}

	public void createSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		StringBuilder externalDeclerations = new StringBuilder("");
		associationExporter.exportAssocations(cls.getOwnedAttributes());
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, cls.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		if (stateMachineExporter.isOwnStateMachine()) {
			submachineMap = stateMachineExporter.getSubMachines();

			for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
				subStateMachineExporter = new SubStateMachineExporter(entry.getValue().getSecond(), name,entry.getValue().getFirst(), dest);
				subStateMachineExporter.createSubSmSource();
				subSubMachines.addAll(subStateMachineExporter.getSubmachines());
			}
		}

		source = createClassHeaderSource();
		externalDeclerations.append(associationExporter.createLinkFunctionDeclerations(name));
		Shared.writeOutSource(dest, GenerationTemplates.headerName(name),
				GenerationTemplates.headerGuard(source.toString() + externalDeclerations.toString(), name));
		source = (createClassCppSource(cls)).toString();
		Shared.writeOutSource(dest, GenerationTemplates.sourceName(name),
				GenerationTemplates.cppInclude(name) + getAllDependency(false) + source);
	}

	public List<String> getSubmachines() {
		List<String> ret = new LinkedList<String>();
		if (submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
				ret.add(entry.getValue().getFirst());
			}
			ret.addAll(subSubMachines);
		}
		return ret;
	}

	public void setRealName(String realClassName) {
		this.name = realClassName;

	}

	private String createClassHeaderSource() {
		String source = "";
		StringBuilder dependency = getAllDependency(true);
		StringBuilder privateParts = createParts("private");
		StringBuilder protectedParts = createParts("protected");
		StringBuilder publicParts = createParts("public");

		if (!ownConstructor) {
			publicParts.append(GenerationTemplates.constructorDecl(name, null) + "\n");
		}

		publicParts.append("\n" + associationExporter.createAssociationMemeberDeclerationsCode());

		if (associationExporter.ownAssociation()) {
			publicParts.append(
					GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Link));
			publicParts.append(
					GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Unlink));
		}

		if (stateMachineExporter.isOwnStateMachine()) {

			publicParts.append(stateMachineExporter.createStateEnumCode());
			privateParts.append(stateMachineExporter.createStateMachineRelatedHeadedDeclerationCodes());

			if (submachineMap.isEmpty()) {
				source = GenerationTemplates
						.simpleStateMachineClassHeader(dependency.toString(), name, getBaseClass(), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), true)
						.toString();
			} else {
				source = GenerationTemplates.hierarchicalStateMachineClassHeader(dependency.toString(), name,
						getBaseClass(), getSubmachines(), publicParts.toString(), protectedParts.toString(),
						privateParts.toString(), true).toString();
			}
		} else {
			source = GenerationTemplates.classHeader(dependency.toString(), name, getBaseClass(),
					publicParts.toString(), protectedParts.toString(), privateParts.toString(), true).toString();
		}
		return source;
	}

	private StringBuilder createClassCppSource(Class class_) {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);

		if (!ownConstructor) {
			source.append(GenerationTemplates.constructorDef(name, stateMachineExporter.isOwnStateMachine()) + "\n");
		}

		if (stateMachineExporter.isOwnStateMachine()) {
			source.append(stateMachineExporter.createStateMachineRelatedCppSourceCodes());

		} else {
			source.append(GenerationTemplates.destructorDef(name, false));
		}

		for (Operation operation : class_.getOwnedOperations()) {
			activityExporter.init();
			String funcBody = "";
			for (Behavior behavior : operation.getMethods()) {

				if (behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
					funcBody = activityExporter.createfunctionBody((Activity) behavior).toString();
				} else {
					// TODO exception, unknown for me, need the model
				}
			}
			if (!Shared.isConstructor(operation)) {

				String returnType = getReturnType(operation.getReturnResult());

				source.append(GenerationTemplates.functionDef(name, returnType, operation.getName(),
						getOperationParams(operation), funcBody));
			} else {

				source.append(GenerationTemplates.constructorDef(name, getBaseClass(), funcBody,
						getOperationParams(operation), null, stateMachineExporter.isOwnStateMachine()));
			}
		}
		return source;
	}

	// TODO string dependency as special case ....
	private StringBuilder getAllDependency(Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		List<String> types = new ArrayList<String>();

		// collecting each item type for dependency analysis
		for (Operation item : cls.getAllOperations()) {
			if (item.getReturnResult() != null) {
				types.add(item.getReturnResult().getType().getName());
			}
			types.addAll(getOperationParamTypes(item));
		}

		for (Property item : Shared.getProperties(cls)) {
			if (item.getType() != null) {

				Type attr = item.getType();
				types.add(attr.getName());
			}
		}

		if (submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
				types.add(entry.getValue().getFirst());
			}
		}

		// dependency analysis
		String header = "";
		for (String t : types) {
			if (!Shared.isBasicType(t) && t != name) {
				if (isHeader) {
					header = GenerationTemplates.forwardDeclaration(t);
				} else {
					if (!t.equals("String")) {
						header = GenerationTemplates.cppInclude(t);
					}
				}

				// TODO this is suboptimal
				if (!source.toString().contains(header)) {
					source.append(header);
				}
			}
		}

		if (getBaseClass() != null) {
			source.append(GenerationTemplates.cppInclude(getBaseClass()));
		}

		if (!isHeader) {
			source.append(GenerationTemplates.cppInclude(GenerationTemplates.DeploymentHeader));
			source.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
			source.append(GenerationTemplates.cppInclude(GenerationTemplates.AssociationsStructuresHreaderName));
			source.append(GenerationTemplates
					.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.StandardFunctionsHeader));
			source.append(GenerationTemplates
					.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.TimerInterfaceHeader));
			source.append(
					GenerationTemplates.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.TimerHeader));
		} else {
			source.append(GenerationTemplates
					.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.AssocationHeader));

		}

		source.append("\n");
		return source;
	}

	private StringBuilder createParts(String modifyer) {
		StringBuilder source = new StringBuilder("");
		for (Operation item : cls.getOwnedOperations()) {
			if (item.getVisibility().toString().equals(modifyer)) {

				if (Shared.isConstructor(item)) {
					ownConstructor = true;
					source.append(GenerationTemplates.constructorDecl(name, getOperationParamTypes(item)));
				} else {
					String returnType = getReturnType(item.getReturnResult());
					source.append(
							GenerationTemplates.functionDecl(returnType, item.getName(), getOperationParamTypes(item)));
				}
			}
		}

		for (Property attribute : cls.getOwnedAttributes()) {
			if (attribute.getVisibility().toString().equals(modifyer)) {
				String type = "!!!UNKNOWNTYPE!!!";
				if (attribute.getType() != null) {
					type = attribute.getType().getName();
				}

				String tmp = GenerationTemplates.variableDecl(type, attribute.getName(), 1);
				if (attribute.getAssociation() == null) {
					source.append(tmp);
				}

			}
		}

		return source;
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

	private List<Pair<String, String>> getOperationParams(Operation operation) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param : operation.getOwnedParameters()) {
			if (param != operation.getReturnResult()) {
				if (param.getType() != null) {
					ret.add(new Pair<String, String>(param.getType().getName(), param.getName()));
				} else {
					// TODO exception if we want to stop the compile (missing
					// operation, seems fatal error)
					ret.add(new Pair<String, String>("UNKNOWN_TYPE", param.getName()));
				}
			}
		}
		return ret;
	}

	private String getReturnType(Parameter returnResult) {
		String returnType = null;
		if (returnResult != null) {
			returnType = returnResult.getType().getName();
		}
		return returnType;
	}

	private String getBaseClass() {
		if (!cls.getGeneralizations().isEmpty()) {
			return cls.getGeneralizations().get(0).getGeneral().getName();
		} else {
			return null;
		}
	}

}
