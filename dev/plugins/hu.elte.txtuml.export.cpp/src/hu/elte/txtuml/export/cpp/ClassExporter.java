package hu.elte.txtuml.export.cpp;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class ClassExporter {
	private static final int _UMLMany = -1;
	private static String _unknownEntryName = "entry";
	private static String _unknownExitName = "exit";

	private Map<String, Pair<String, String>> _entryMap;// <name,<state,funcBody>>
	private Map<String, Pair<String, String>> _exitMap;// <name,<state,funcBody>>
	private Map<String, Pair<String, Region>> _submachineMap;// <stateName,<machinename,behavior>>
	private List<Property> associationMembers;
	private List<String> subSubMachines;
	private List<String> additionalSourcesNames;
	private boolean ownConstructor;

	ActivityExporter activityExporter;
	GuardExporter guardExporter;

	private enum FuncTypeEnum {
		Entry, Exit
	}

	private Integer poolId;
	private String realName;

	public ClassExporter() {
		reiniIialize();
	}

	public void reiniIialize() {
		activityExporter = new ActivityExporter();
		guardExporter = new GuardExporter();
		associationMembers = new ArrayList<Property>();
		additionalSourcesNames = new ArrayList<String>();
		_entryMap = null;
		_exitMap = null;
		_submachineMap = null;
		subSubMachines = new LinkedList<String>();
		ownConstructor = false;

	}

	public List<String> getAdditionalSources() {
		return additionalSourcesNames;
	}

	public void createSource(Class class_, String dest_) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		StringBuilder externalDeclerations = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		if (ownStates(class_, smList)) {
			Region region = smList.get(0).getRegions().get(0);
			_submachineMap = getSubMachines(region);
			createFuncTypeMap(region, FuncTypeEnum.Entry, true);
			createFuncTypeMap(region, FuncTypeEnum.Exit, true);

			for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
				ClassExporter classExporter = new ClassExporter();
				classExporter.setRealName(entry.getValue().getFirst());
				classExporter.createSubSmSource(entry.getValue().getFirst(), realName, entry.getValue().getSecond(),
						dest_);
				subSubMachines.addAll(classExporter.getSubmachines());
			}
		}

		source = createClassHeaderSource(class_);
		externalDeclerations.append(createLinkFunctionDeclerations(class_));
		Shared.writeOutSource(dest_, GenerationTemplates.headerName(realName),
				GenerationTemplates.headerGuard(source.toString() + externalDeclerations.toString(), realName));
		source = (createClassCppSource(class_)).toString();
		Shared.writeOutSource(dest_, GenerationTemplates.sourceName(realName),
				GenerationTemplates.cppInclude(realName) + getAllDependency(class_, false) + source);
	}

	public List<String> getSubmachines() {
		List<String> ret = new LinkedList<String>();
		if (_submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
				ret.add(entry.getValue().getFirst());
			}
			ret.addAll(subSubMachines);
		}
		return ret;
	}

	public void setConfiguratedPoolId(Integer poolId) {
		this.poolId = poolId;

	}

	public void setRealName(String realClassName) {
		this.realName = realClassName;

	}

	private void createSubSmSource(String className_, String parentClass, Region region_, String dest_)
			throws FileNotFoundException, UnsupportedEncodingException {
		String source = "";
		_submachineMap = getSubMachines(region_);
		createFuncTypeMap(region_, FuncTypeEnum.Entry, false);
		createFuncTypeMap(region_, FuncTypeEnum.Exit, false);

		for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
			createSubSmSource(entry.getValue().getFirst(), parentClass, entry.getValue().getSecond(), dest_);
		}

		source = createSubSmClassHeaderSource(className_, parentClass, region_);
		Shared.writeOutSource(dest_, GenerationTemplates.headerName(className_),
				GenerationTemplates.headerGuard(source, className_));
		source = createSubSmClassCppSource(className_, parentClass, region_).toString();

		String dependencyIncludes = GenerationTemplates.cppInclude(className_);
		dependencyIncludes = GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude)
				+ dependencyIncludes + GenerationTemplates.cppInclude(parentClass);

		Shared.writeOutSource(dest_, GenerationTemplates.sourceName(className_), dependencyIncludes + "\n" + source);
	}

	private String createClassHeaderSource(Class class_) {
		String source = "";
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		StringBuilder dependency = getAllDependency(class_, true);
		StringBuilder privateParts = createParts(class_, "private");
		StringBuilder protectedParts = createParts(class_, "protected");
		StringBuilder publicParts = createParts(class_, "public");

		if (!ownConstructor) {
			publicParts.append(GenerationTemplates.constructorDecl(realName, null) + "\n");
		}

		publicParts.append("\n" + getAssocations(class_));

		if (!associationMembers.isEmpty()) {
			publicParts.append(
					GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Link));
			publicParts.append(
					GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Unlink));
		}

		if (ownStates(class_, smList)) {
			Region region = smList.get(0).getRegions().get(0);
			privateParts.append(createEntryFunctionsDecl(region));
			privateParts.append(createExitFunctionsDecl(region));
			privateParts.append(declareGuardFunctions(region));
			privateParts.append(createTransitionFunctionDecl(region));
			publicParts.append(GenerationTemplates.stateEnum(getStateList(region), getInitialState(region)));

			if (_submachineMap.isEmpty()) {
				source = GenerationTemplates
						.simpleStateMachineClassHeader(dependency.toString(), realName, getBaseClass(class_), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), true)
						.toString();
			} else {
				source = GenerationTemplates.hierarchicalStateMachineClassHeader(dependency.toString(), realName,
						getBaseClass(class_), getSubmachines(), publicParts.toString(), protectedParts.toString(),
						privateParts.toString(), true).toString();
			}
		} else {
			source = GenerationTemplates.classHeader(dependency.toString(), realName, getBaseClass(class_),
					publicParts.toString(), protectedParts.toString(), privateParts.toString(), true).toString();
		}
		return source;
	}

	private String createSubSmClassHeaderSource(String className_, String parentClass, Region region_) {
		String source = "";
		StringBuilder dependency = new StringBuilder(GenerationTemplates.cppInclude(parentClass));
		dependency.append(GenerationTemplates.cppInclude(GenerationTemplates.StandardFunctionsHeader));

		StringBuilder privateParts = createEntryFunctionsDecl(region_);
		privateParts.append(createExitFunctionsDecl(region_));
		privateParts.append(GenerationTemplates.formatSubSmFunctions(declareGuardFunctions(region_).toString()));
		privateParts.append(createTransitionFunctionDecl(region_));
		String protectedParts = "";

		StringBuilder publicParts = new StringBuilder("");
		List<String> params = new ArrayList<String>();
		params.add(parentClass);
		publicParts.append(GenerationTemplates.constructorDecl(className_, params));
		publicParts.append(GenerationTemplates.stateEnum(getStateList(region_), getInitialState(region_)));

		if (_submachineMap.isEmpty()) {
			source = GenerationTemplates.simpleSubStateMachineClassHeader(dependency.toString(), className_,
					parentClass, publicParts.toString(), protectedParts, privateParts.toString()).toString();
		} else {
			source = GenerationTemplates
					.hierarchicalSubStateMachineClassHeader(dependency.toString(), className_, parentClass,
							getSubmachines(), publicParts.toString(), protectedParts, privateParts.toString())
					.toString();
		}
		return source;
	}

	private StringBuilder createClassCppSource(Class class_) {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);

		if (!ownConstructor) {
			source.append(GenerationTemplates.constructorDef(realName, ownStates(class_, smList)) + "\n");
		}

		if (ownStates(class_, smList)) {
			Region region = smList.get(0).getRegions().get(0);
			Multimap<Pair<String, String>, Pair<String, String>> smMap = createMachine(region);
			if (_submachineMap.isEmpty()) {
				source.append(GenerationTemplates.simpleStateMachineInitialization(realName, getInitialState(region),
						true, poolId, smMap));
				source.append(GenerationTemplates.simpleStateMachineFixFunctionDefnitions(realName,
						getInitialState(region), false));

			} else {
				source.append(GenerationTemplates.hierachialStateMachineInitialization(realName,
						getInitialState(region), true, poolId, smMap, getEventSubmachineNameMap()));
				source.append(GenerationTemplates.hiearchialStateMachineFixFunctionDefinitions(realName,
						getInitialState(region), false));

			}

			source.append(GenerationTemplates.destructorDef(realName, true));
			source.append(defnieGuardFunctions());
			source.append(createEntryFunctionsDef(realName, region));
			source.append(createExitFunctionsDef(realName, region));
			source.append(createTransitionFunctionsDef(realName, region, true));

			source.append(GenerationTemplates.entry(realName, createStateActionMap(_entryMap, region)) + "\n");
			source.append(GenerationTemplates.exit(realName, createStateActionMap(_exitMap, region)) + "\n");

		} else {
			source.append(GenerationTemplates.destructorDef(realName, false));
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

				source.append(GenerationTemplates.functionDef(realName, returnType, operation.getName(),
						getOperationParams(operation), funcBody));
			} else {

				source.append(GenerationTemplates.constructorDef(realName, getBaseClass(class_), funcBody,
						getOperationParams(operation), null, ownStates(class_, smList)));
			}
		}
		return source;
	}

	private StringBuilder createSubSmClassCppSource(String className_, String parentStateMachine, Region region_) {
		StringBuilder source = new StringBuilder("");
		Multimap<Pair<String, String>, Pair<String, String>> smMap = createMachine(region_);
		if (_submachineMap.isEmpty()) {
			source.append(GenerationTemplates.simpleSubStateMachineClassConstructor(className_, parentStateMachine,
					smMap, getInitialState(region_)));
		} else {
			source.append(GenerationTemplates.hierarchicalSubStateMachineClassConstructor(className_,
					parentStateMachine, smMap, getEventSubmachineNameMap(), getInitialState(region_)));
		}
		source.append(GenerationTemplates.destructorDef(className_, false));
		StringBuilder subSmSpec = createEntryFunctionsDef(className_, region_);
		subSmSpec.append(createExitFunctionsDef(className_, region_));
		subSmSpec.append(defnieGuardFunctions());
		subSmSpec.append(createTransitionFunctionsDef(className_, region_, false));
		subSmSpec.append(GenerationTemplates.entry(className_, createStateActionMap(_entryMap, region_)) + "\n");
		subSmSpec.append(GenerationTemplates.exit(className_, createStateActionMap(_exitMap, region_)) + "\n");

		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));
		return source;
	}

	private String createLinkFunctionDeclerations(Class class_) {
		StringBuilder assocDeclerations = new StringBuilder("");
		for (Property member : associationMembers) {
			if (member.isNavigable()) {
				assocDeclerations.append(GenerationTemplates.linkTemplateSpecializationDecl(realName,
						member.getType().getName(), member.getName(), member.getAssociation().getName(),
						GenerationTemplates.LinkFunctionType.Link));
				assocDeclerations.append(GenerationTemplates.linkTemplateSpecializationDecl(realName,
						member.getType().getName(), member.getName(), member.getAssociation().getName(),
						GenerationTemplates.LinkFunctionType.Unlink));
			}

		}
		return GenerationTemplates.cppInclude(GenerationTemplates.AssociationsStructuresHreaderName)
				+ assocDeclerations.toString();
	}

	private Map<String, Pair<String, Region>> getSubMachines(Region region_) {
		Map<String, Pair<String, Region>> submachineMap = new HashMap<String, Pair<String, Region>>();
		for (State state : getStateList(region_)) {
			// either got a submachine or a region, both is not permitted
			StateMachine m = state.getSubmachine();
			if (m != null) {
				submachineMap.put(state.getName(), new Pair<String, Region>(m.getName(), m.getRegions().get(0)));
			} else {
				List<Region> r = state.getRegions();
				if (!r.isEmpty()) {
					submachineMap.put(state.getName(), new Pair<String, Region>(state.getName() + "_subSM", r.get(0)));
				}
			}
		}
		return submachineMap;
	}

	private void createFuncTypeMap(Region region, FuncTypeEnum funcType_, Boolean rt_) {
		Map<String, Pair<String, String>> map = new HashMap<String, Pair<String, String>>();
		String source = "";
		String name = "";
		for (State item : getStateList(region)) {
			Behavior behavior = null;
			String unknownName = null;
			switch (funcType_) {
			case Entry: {
				behavior = item.getEntry();
				unknownName = _unknownEntryName;
				break;
			}
			case Exit: {
				behavior = item.getExit();
				unknownName = _unknownExitName;
				break;
			}
			}

			if (behavior != null) {
				if (behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
					activityExporter.init();
					source = activityExporter.createfunctionBody((Activity) behavior).toString();
					name = item.getName() + "_" + unknownName;
					map.put(name, new Pair<String, String>(item.getName(), source.toString()));
				}
			}
		}

		if (funcType_ == FuncTypeEnum.Entry) {
			_entryMap = map;
		} else if (funcType_ == FuncTypeEnum.Exit) {
			_exitMap = map;
		}

	}

	private StringBuilder createEntryFunctionsDecl(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _entryMap.entrySet()) {
			source.append(GenerationTemplates.functionDecl(entry.getKey()));
		}
		return source;
	}

	private StringBuilder createExitFunctionsDecl(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _exitMap.entrySet()) {
			source.append(GenerationTemplates.functionDecl(entry.getKey()));
		}
		return source;
	}

	private StringBuilder declareGuardFunctions(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region_.getTransitions()) {
			Constraint constraint = item.getGuard();
			if (constraint != null) {
				// TODO else..
				guardExporter.exportConstraintToMap(constraint);
				source.append(GenerationTemplates.guardDecleration(guardExporter.getGuard(constraint)));
			}
		}
		source.append("\n");
		return source;
	}

	private StringBuilder defnieGuardFunctions() {
		StringBuilder source = new StringBuilder("");
		for (Entry<Constraint, String> guardEntry : guardExporter.getGuards().entrySet()) {
			guardExporter.init();
			String body = guardExporter.getGuardFromValueSpecification(guardEntry.getKey().getSpecification());
			source.append(GenerationTemplates.guardDefinition(guardEntry.getValue(), body, realName,
					guardExporter.isContainsSignalAcces()));
		}

		return source;
	}

	// TODO string dependency as special case ....
	private StringBuilder getAllDependency(Class class_, Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		List<String> types = new ArrayList<String>();

		// collecting each item type for dependency analysis
		for (Operation item : class_.getAllOperations()) {
			if (item.getReturnResult() != null) {
				types.add(item.getReturnResult().getType().getName());
			}
			types.addAll(getOperationParamTypes(item));
		}

		for (Property item : Shared.getProperties(class_)) {
			if (item.getType() != null) {

				Type attr = item.getType();
				types.add(attr.getName());
			}
		}

		if (_submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
				types.add(entry.getValue().getFirst());
			}
		}

		// dependency analysis
		String header = "";
		for (String t : types) {
			if (!Shared.isBasicType(t) && t != realName) {
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

		if (getBaseClass(class_) != null) {
			source.append(GenerationTemplates.cppInclude(getBaseClass(class_)));
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

	private StringBuilder createTransitionFunctionDecl(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region_.getTransitions()) {
			source.append(GenerationTemplates.transitionActionDecl(item.getName()));
		}
		source.append("\n");
		return source;
	}

	private StringBuilder createTransitionFunctionsDef(String className_, Region region_, Boolean rt_) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region_.getTransitions()) {
			String body = "";
			activityExporter.init();
			Behavior b = item.getEffect();
			if (b != null && b.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
				Activity a = (Activity) b;
				body = activityExporter.createfunctionBody(a).toString();
			}

			Pair<String, Boolean> setState = createSetState(item);
			source.append(GenerationTemplates.transitionActionDef(className_, item.getName(),
					body + setState.getFirst() + "\n",
					activityExporter.isContainsSignalAcces() || setState.getSecond()));
		}
		source.append("\n");
		return source;
	}

	/*
	 * handle the choice in the statemachine looks: state -transition-
	 * choiceNode < (guard1/tran1) (guard2/tran2)
	 */
	private Pair<String, Boolean> createSetState(Transition transition_) {
		String source = "";
		boolean containsChoice = false;
		Vertex targetState = transition_.getTarget();

		// choice handling
		if (targetState.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)
				&& ((Pseudostate) targetState).getKind().equals(PseudostateKind.CHOICE_LITERAL)) {
			List<Pair<String, String>> branches = new LinkedList<Pair<String, String>>();
			Pair<String, String> elseBranch = null;
			containsChoice = true;
			for (Transition trans : targetState.getOutgoings()) {

				String guard = guardExporter.getGuard(trans.getGuard()) + "(" + GenerationTemplates.eventParamName()
						+ ")";
				String body = ActivityTemplates.blockStatement(ActivityTemplates.transitionActionCall(trans.getName()))
						.toString();

				if (guard.isEmpty() || guard.equals("else")) {
					elseBranch = new Pair<String, String>(guard, body);
				} else {
					branches.add(new Pair<String, String>(guard, body));
				}
			}
			if (elseBranch != null) {
				branches.add(elseBranch);
			}
			source = ActivityTemplates.elseIf(branches).toString();
		} else if (targetState.eClass().equals(UMLPackage.Literals.STATE)) {
			source = GenerationTemplates.setState(targetState.getName());

		} else {
			source = GenerationTemplates.setState("UNKNOWN_TRANSITION_TARGET");
		}
		return new Pair<String, Boolean>(source, containsChoice);
	}

	private StringBuilder createParts(Class class_, String modifyer_) {
		StringBuilder source = new StringBuilder("");
		for (Operation item : class_.getOwnedOperations()) {
			if (item.getVisibility().toString().equals(modifyer_)) {

				if (Shared.isConstructor(item)) {
					ownConstructor = true;
					source.append(GenerationTemplates.constructorDecl(realName, getOperationParamTypes(item)));
				} else {
					String returnType = getReturnType(item.getReturnResult());
					source.append(
							GenerationTemplates.functionDecl(returnType, item.getName(), getOperationParamTypes(item)));
				}
			}
		}

		for (Property attribute : class_.getOwnedAttributes()) {
			if (attribute.getVisibility().toString().equals(modifyer_)) {
				String type = "!!!UNKNOWNTYPE!!!";
				if (attribute.getType() != null) {
					type = attribute.getType().getName();
				}

				String tmp = GenerationTemplates.variableDecl(type, attribute.getName(), 1);
				// TODO suboptimal code
				if (!source.toString().contains(tmp) && attribute.getAssociation() == null) {
					source.append(tmp);
				}

			}
			// TODO else exception if we want to stop the compile
		}

		return source;
	}

	private StringBuilder getAssocations(Class class_) {
		StringBuilder source = new StringBuilder("");
		for (Property prop : class_.getOwnedAttributes()) {
			if(prop.getAssociation() != null){
				int upper = prop.getUpper();
				int lower = prop.getLower();

				if (prop.getUpper() == _UMLMany) {
					upper = Integer.MAX_VALUE;
				}

				String linkedClass = GenerationTemplates.assocationDecl(prop.getType().getName(),
						GenerationTemplates.formatAssociationRoleName(prop.getAssociation().getName(), prop.getName()),
						lower, upper);
				associationMembers.add(prop);
				if (prop.isNavigable())
					source.append(linkedClass);
			}				

		}
		return source;
	}

	private List<String> getOperationParamTypes(Operation op_) {
		List<String> ret = new ArrayList<String>();
		for (Parameter param : op_.getOwnedParameters()) {
			if (param != op_.getReturnResult()) {
				if (param.getType() != null) {
					ret.add(param.getType().getName());
				}
			}
		}
		return ret;
	}

	private List<Pair<String, String>> getOperationParams(Operation op_) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param : op_.getOwnedParameters()) {
			if (param != op_.getReturnResult()) {
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

	private Map<String, String> getEventSubmachineNameMap() {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
			ret.put(entry.getKey(), entry.getValue().getFirst());
		}
		return ret;
	}

	private Map<String, String> createStateActionMap(Map<String, Pair<String, String>> map_, Region region_) {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, String>> entry : map_.entrySet()) {
			ret.put(entry.getValue().getFirst(), entry.getKey());
		}
		return ret;
	}

	private StringBuilder createEntryFunctionsDef(String className_, Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _entryMap.entrySet()) {
			source.append(GenerationTemplates.functionDef(className_, entry.getKey(), entry.getValue().getSecond()));
		}
		return source;
	}

	private StringBuilder createExitFunctionsDef(String className_, Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _exitMap.entrySet()) {
			source.append(GenerationTemplates.functionDef(className_, entry.getKey(), entry.getValue().getSecond()));
		}
		return source;
	}

	private String getReturnType(Parameter returnResult_) {
		String returnType = null;

		if (returnResult_ != null) {
			returnType = returnResult_.getType().getName();
		}
		return returnType;
	}

	private String getInitialState(Region region_) {
		String source = "NO_INITIAL_STATE";
		for (Vertex item : region_.getSubvertices()) {

			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {

				Pseudostate pseduoState = (Pseudostate) item;
				if (pseduoState.getKind().equals(PseudostateKind.INITIAL_LITERAL)) {
					source = item.getName();
				}

			}
		}
		return source;
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */
	private Multimap<Pair<String, String>, Pair<String, String>> createMachine(Region region_) {
		Multimap<Pair<String, String>, Pair<String, String>> smMap = HashMultimap.create();
		for (Transition item : region_.getTransitions()) {
			Pair<String, String> eventSignalPair = null;

			if (item.getSource().getName().equals(getInitialState(region_))) {
				eventSignalPair = new Pair<String, String>(GenerationTemplates.InitSignal, item.getSource().getName());
			}

			for (Trigger tri : item.getTriggers()) {
				Event e = tri.getEvent();
				if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
					SignalEvent se = (SignalEvent) e;
					if (se != null) {
						eventSignalPair = new Pair<String, String>(se.getSignal().getName(),
								item.getSource().getName());
					}
				}
			}
			if (eventSignalPair != null) {
				Pair<String, String> guardTransitionPair = null;
				if (item.getGuard() != null) {
					guardTransitionPair = new Pair<String, String>(guardExporter.getGuard(item.getGuard()),
							item.getName());

				} else {
					guardTransitionPair = new Pair<String, String>(null, item.getName());
				}
				smMap.put(eventSignalPair, guardTransitionPair);
			}
		}
		return smMap;
	}

	private List<State> getStateList(Region region_) {
		List<State> stateList = new ArrayList<State>();
		for (Vertex item : region_.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.STATE)) {
				stateList.add((State) item);
			}
		}
		return stateList;
	}

	private boolean ownStates(Class class_, List<StateMachine> smList) {

		if (smList.isEmpty() || getStateList(smList.get(0).getRegions().get(0)).size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	@SuppressWarnings("unused")
	private Set<SignalEvent> getEventList(Region region_) {
		Set<SignalEvent> eventList = new HashSet<SignalEvent>();
		for (Transition item : region_.getTransitions()) {
			for (Trigger tri : item.getTriggers()) {
				Event e = tri.getEvent();
				if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
					SignalEvent se = (SignalEvent) e;
					if (se != null && !eventList.contains(se)) {
						eventList.add(se);
					}
				}
			}
		}

		// get the submachine events
		Map<String, Pair<String, Region>> submachineMap = getSubMachines(region_);
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			eventList.addAll(getEventList(entry.getValue().getSecond()));
		}

		return eventList;
	}

	private String getBaseClass(Class class_) {
		class_.getGenerals();

		if (!class_.getGeneralizations().isEmpty()) {
			return class_.getGeneralizations().get(0).getGeneral().getName();
		} else {
			return null;
		}
	}

}
