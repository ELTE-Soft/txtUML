package txtuml.importer;

import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import txtuml.core.*;
import txtuml.core.instructions.*;
import txtuml.api.*;
import txtuml.utils.InstanceCreator;

// note on thread safety: during importation only thread is used
public class Importer {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("One command line argument needed.");
			return;
		}

		try {
			importModel(args[0]);
		} catch(ImportException ie) {
			System.out.println("Error: " + ie.getMessage());
		}
	}

	public static CoreModel importModel(String className) throws ImportException {
		Class<?> modelClass = findModel(className);
		
		CoreModel model = new CoreModel(modelClass.getSimpleName());
		
        currentModel = model;
        // Import class names
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
				model.addClassName(c.getSimpleName());
			}
		}
        // Import associations
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isAssociation(c)) {
                model.addAssociation(importAssociation(c,model));
			}
		}
        // Import events
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isEvent(c)) {
                model.addSignal(new CoreSignal(c.getSimpleName()));
			}
		}
        // Import method names
        for(Method m : modelClass.getDeclaredMethods()) {
            model.addMethodName(m.getName());
        }
        // Import class attributes
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                CoreClass coreClass = model.getClass(c.getSimpleName());
				for(Field f : c.getDeclaredFields()) {
                    if(isAttribute(f)) {
                        coreClass.addAttribute(importAttribute(f));
                    }
                }
			}
		}
        // Import member function names
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                CoreClass coreClass = model.getClass(c.getSimpleName());
				for(Method m : c.getDeclaredMethods()) {
                    if(isMemberFunction(m)) {
                        coreClass.addMethod(m.getName());
                    }
                }
			}
		}
        // Import method bodies
		for(Method m : modelClass.getDeclaredMethods()) {
			CoreMethod coreMethod = model.getMethod(m.getName());
            if(coreMethod != null) {
                importMethod(coreMethod,m,modelClass);
			}
		}
        // Import class method bodies
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                CoreClass coreClass = model.getClass(c.getSimpleName());
				for(Method m : c.getDeclaredMethods()) {
                    CoreMethod coreMethod = coreClass.getMethod(m.getName());
                    if(coreMethod != null) {
                        importMethod(coreMethod,m,c);
                    }
                }
			}
		}
        // Import class state machines
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                CoreClass coreClass = model.getClass(c.getSimpleName());
                coreClass.setStateMachine(importStateMachine(c,model));
			}
		}
        currentModel = null;
		return model;
	}

    static CoreClass importClass() {
        return null;
    }

    static Class<?> findModel(String className) throws ImportException {
		try {
			Class<?> ret = Class.forName(className);
			if(!Model.class.isAssignableFrom(ret)) {
				//throw new ImportException("A subclass of Model is expected, got: " + className);
			}
			return ret;
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }
	
	static boolean isClass(Class<?> c) {
		return ModelClass.class.isAssignableFrom(c);
	}

	static boolean isAssociation(Class<?> c) {
		return Association.class.isAssignableFrom(c);
	}
    
	static boolean isEvent(Class<?> c) {
		return Signal.class.isAssignableFrom(c);
	}

    static boolean isAttribute(Field f) {
        return ModelType.class.isAssignableFrom(f.getType());   
    }
    
    static boolean isState(Class<?> c) {
        return ModelClass.State.class.isAssignableFrom(c);
    }

    static boolean isInitialState(Class<?> c) {
        return ModelClass.InitialState.class.isAssignableFrom(c);
    }

    static boolean isCompositeState(Class<?> c) {
        return ModelClass.CompositeState.class.isAssignableFrom(c);
    }

    static boolean isTransition(Class<?> c) {
        return ModelClass.Transition.class.isAssignableFrom(c);
    }
    
    static boolean isMemberFunction(Method m) {
        return m.getParameterTypes().length == 0; // TODO remove when parameters are handled
    }
    
    static CoreAssociation importAssociation(Class<?> c, CoreModel m) throws ImportException {
        List<Field> fs = new LinkedList<Field>(Arrays.asList(c.getDeclaredFields()));
        try {
            fs.remove(c.getDeclaredField("this$0"));
        } catch( NoSuchFieldException n ) {
        }
        if(fs.size() != 2) {
            throw new ImportException("Associations must have exactly two fields. "
                                      + c.getSimpleName() + " has "
                                      + Integer.toString(fs.size()) + ".");
        }
        return new CoreAssociation(c.getSimpleName(),
                               importAssociationEnd(fs.get(0),m),
                               importAssociationEnd(fs.get(1),m));
    }

    static CoreAssociationEnd importAssociationEnd(Field f, CoreModel m) throws ImportException {
        String phrase = f.getName();
        String className = f.getType().getSimpleName();
        CoreClass participant = m.getClass(className);
        if(participant == null) {
            throw new ImportException(phrase + ": No class " + className + " found in this model.");
        }
        Annotation[] as = f.getDeclaredAnnotations();
        if(as.length != 1) {
            throw new ImportException(phrase + ": cannot determine multiplicity (Number of annotations must be 1, found "
                                             + Integer.toString(as.length) + ").");
        }
        CoreMultiplicity multiplicity;
        if(as[0].annotationType().equals(txtuml.api.One.class)) {
            multiplicity = CoreMultiplicity.One;
        } else if(as[0].annotationType().equals(txtuml.api.MaybeOne.class)) {
            multiplicity = CoreMultiplicity.MaybeOne;
        } else if(as[0].annotationType().equals(txtuml.api.Some.class)) {
            multiplicity = CoreMultiplicity.Some;
        } else if(as[0].annotationType().equals(txtuml.api.Many.class)) {
            multiplicity = CoreMultiplicity.Many;
        } else {
            throw new ImportException(phrase + ": has invalid multiplicity.");            
        }
        return new CoreAssociationEnd(participant,phrase,multiplicity);
    }

	static void importMethod(CoreMethod coreMethod, Method sourceMethod,
                             Class<?> declaringClass, Object... params) {
		instructionBuffer = coreMethod;
		sourceMethod.setAccessible(true);
        setLocalInstanceToBeCreated(true);
		Object o = InstanceCreator.createInstance(declaringClass,3);
        setLocalInstanceToBeCreated(false);
        if(o instanceof ModelClass) {
            coreMethod.setSelf(new CoreInstance(((ModelClass)o).getIdentifier()));
        }
		try {
			sourceMethod.invoke(o,params);
		} catch(Exception e) {
			System.out.println(sourceMethod.getName());
			System.out.println(o.getClass().getSimpleName());
			e.printStackTrace();
		}
		instructionBuffer = null;
	}

    static CoreAttribute importAttribute(Field f) {
        return new CoreAttribute(f.getName(), importType(f.getType()));
    }
    
    static CoreDataType importType(Class<?> t) {
        if(t == ModelInt.class) {
            return CoreDataType.IntType;
        }
        if(t == ModelBool.class) {
            return CoreDataType.BoolType;
        }
        if(t == ModelString.class) {
            return CoreDataType.StringType;
        }
        return null;
    }
    
    static CoreStateMachine importStateMachine(Class<?> sourceClass, CoreModel model) throws ImportException {
        CoreStateMachine result = new CoreStateMachine();
        for(Class<?> c : sourceClass.getDeclaredClasses()) {
            if(isState(c)) {
            	CoreState st;
                if(isInitialState(c)) {
        			if (result.getInitialState() != null) {
                    	throw new ImportException(sourceClass.getName() + " has two initial states: " + result.getInitialState().getName() + ", " + c.getName());
        			}
                	st = new CoreState(c.getSimpleName(),true);
                	result.setInitialState(st);
                } else {
                	st = importState(c,model);
                }
                result.addState(st);
            }
        }
        for(Class<?> c: sourceClass.getDeclaredClasses()) {
        	 if(isTransition(c)) {
        		 result.addTransition(importTransition(c,result,model));
             }
        }
        if(result.getInitialState() == null) {
        	if (result.getStates().size() != 0) {
        		importWarning(sourceClass.getName() + " has one or more states but no initial state (state machine will not be created)");
        	}
        	return null;
        }
        return result;
    }
    
    static CoreState importState(Class<?> state, CoreModel model) throws ImportException { // not initial
    	String stateName = state.getSimpleName();
        CoreMethod entry = new CoreMethod(stateName + "_entry");
        CoreMethod exit = new CoreMethod(stateName + "_exit");
		try {
	        importMethod(entry, state.getMethod("entry"), state);
	        importMethod(exit, state.getMethod("exit"), state);
		} catch (NoSuchMethodException e) {
			e.printStackTrace(); // not possible
		}
		if (isCompositeState(state)) {
			return new CoreState(stateName, entry, exit, importStateMachine(state, model));
		} else {
			return new CoreState(stateName, entry, exit);
		}
    }

    static CoreTransition importTransition(Class<?> trans, CoreStateMachine stateMachine, CoreModel model) throws ImportException {
		String trName = trans.getSimpleName();
		From fromAnnot = trans.getAnnotation(From.class);
		if (fromAnnot == null) {
			throw new ImportException("transition " + trans.getSimpleName() + " has no @From annotation");
		}
		To toAnnot = trans.getAnnotation(To.class);
		if (toAnnot == null) {
			throw new ImportException("transition " + trans.getSimpleName() + " has no @To annotation");
		}
		if (fromAnnot.value().getEnclosingClass() != toAnnot.value().getEnclosingClass()) {
			throw new ImportException("the two states connected by transition " + trans.getSimpleName() + " are not part of the same state machine or composite state");
		}
		CoreState from = stateMachine.getState(fromAnnot.value().getSimpleName());
        CoreState to = stateMachine.getState(toAnnot.value().getSimpleName());
        if (to.isInitial()) {
			throw new ImportException("transition " + trans.getSimpleName() + " goes to an initial state");
        }
        Trigger triggerAnnot = trans.getAnnotation(Trigger.class);
        CoreSignal signal = null;
        if (triggerAnnot == null) {
        	if (!from.isInitial()) {
    			throw new ImportException("transition " + trans.getSimpleName() + " has no @Trigger annotation which is only allowed from an initial state");
        	}
        } else {
        	signal = model.getSignal(triggerAnnot.value().getSimpleName());
        }
        CoreMethod effect = new CoreMethod(trName + "_effect");
        CoreMethod guard = new CoreMethod(trName + "_guard");
        
        try {
        	importMethod(effect, trans.getDeclaredMethod("effect"), trans);
        } catch (NoSuchMethodException e) {
        	effect = null; // effect is not redefined
        }
        try {
        	importMethod(guard,  trans.getDeclaredMethod("guard"), trans);
        	if (from.isInitial()) {
        		throw new ImportException("transition " + trans.getSimpleName() + " goes from an initial state and redefines its guard which is not allowed");
        	}
        } catch (NoSuchMethodException e) {
        	guard = null; // guard is not redefined
        }
        return new CoreTransition(signal, from, to, effect, guard);
    }
    
    public static Signal createSignal(Class<? extends ModelClass.Transition> tr) {
    	Trigger triggerAnnot = tr.getAnnotation(Trigger.class);
    	if (triggerAnnot != null) {
        	return InstanceCreator.createInstance(triggerAnnot.value(), 3);
    	}
		return null;
    }
    
    public static boolean createInstance(ModelClass created) {
        if(instructionBuffer != null) {
            if(!localInstanceToBeCreated) {
                CoreClass type = currentModel.getClass(created.getClass().getSimpleName());
                CoreInstance ref = new CoreInstance(created.getIdentifier());
                instructionBuffer.addInstruction(new CreateInstruction(type,ref));
			}
            return true;
        }
        return false;
    }

	public static boolean delete(ModelClass obj) {
        if(instructionBuffer != null) {
            CoreClass type = currentModel.getClass(obj.getClass().getSimpleName());
            CoreInstance ref = new CoreInstance(obj.getIdentifier());
            instructionBuffer.addInstruction(new DeleteInstruction(type,ref));
            return true;
        }
		return false;
	}
    
    public static boolean link(Class<?> assoc, String leftPhrase, ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        if(instructionBuffer != null) {
            CoreInstance left = new CoreInstance(leftObj.getIdentifier());
            CoreInstance right = new CoreInstance(rightObj.getIdentifier());
            CoreAssociation asc = currentModel.getAssociation(assoc.getSimpleName());
            instructionBuffer.addInstruction(new LinkInstruction(left,right,asc));
            return true;
        }
        return false;
    }

    public static boolean unLink(Class<?> assoc, String leftPhrase, ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        if(instructionBuffer != null) {
            CoreInstance left = new CoreInstance(leftObj.getIdentifier());
            CoreInstance right = new CoreInstance(rightObj.getIdentifier());
            CoreAssociation asc = currentModel.getAssociation(assoc.getSimpleName());
            instructionBuffer.addInstruction(new UnLinkInstruction(left,right,asc));
            return true;
        }
        return false;
    }
    
    public static Object call(ModelClass target, String methodName, Object... args) {
    	// this method is called at every method call where the target object is of any type that extends ModelClass 
    	// parameters: the target object, the name of the called method and the given parameters
        CoreInstance o = new CoreInstance(target.getIdentifier());
        CoreClass c = currentModel.getClass(target.getClass().getSimpleName());
        CoreMethod m = c.getMethod(methodName);
        instructionBuffer.addInstruction(new CallInstruction(o,m));
        return null;
        // TODO should return an instance of the actual return type of the called method
        // it can be get through its Method class
        // the imported model will get this returned object as the result of the method call
        // (if the called method is a void, a null can be returned) 
    }
	
    public static Object callExternal(Class<?> c, String methodName, Object... args) {
		// this method is called before any STATIC call where the target class does NOT implement the ModelElement interface 
    	// parameters: the target class, the name of the called method and the given parameters
    	return null;
        // TODO not implemented; should return an instance of the actual return type of the called method
        // it can be get through its Method class
        // the imported model will get this returned object as the result of the method call
	}
    
    public static void fieldGet(ModelClass target, String fieldName) {
    	// TODO not implemented
    	// this method is called BEFORE any field get on a ModelClass object
    	// if this method changes the value of the actual field, the model will get that value as the result of this field get
    }

    public static void fieldSet(ModelClass target, String fieldName, Object newValue) {
    	// TODO not implemented
    	// this method is called BEFORE any field set on a ModelClass object
    }
    
	public static ModelClass selectOne(ModelClass start, Class<? extends Association> assocClass, String phrase) {
        Field[] fields = assocClass.getDeclaredFields();
        ModelClass result = null;
		for(Field field : fields) {
			if(field.getName().equals(phrase)) {
                setLocalInstanceToBeCreated(true);
				result = (ModelClass)InstanceCreator.createInstance(field.getType(),3);
                setLocalInstanceToBeCreated(false);
                break;
			}
		}            
        CoreInstance st = new CoreInstance(start.getIdentifier());
        CoreAssociation asc = currentModel.getAssociation(assocClass.getSimpleName());
        CoreInstance res = new CoreInstance(result.getIdentifier());
        instructionBuffer.addInstruction(new SelectOneInstruction(st,asc,phrase,res));
        return result;
	}

	public static boolean send(ModelClass receiver, Signal event) {
		if(instructionBuffer != null) {
            CoreSignal ev = currentModel.getSignal(event.getClass().getSimpleName());
            CoreInstance rec = new CoreInstance(receiver.getIdentifier());
            instructionBuffer.addInstruction(new SendInstruction(ev,rec));
			return true;
		}
		return false;
	}

	public static boolean log(String message) { // user log
		if(instructionBuffer != null) {
            instructionBuffer.addInstruction(new LogInstruction(message));
			return true;
		}
		return false;
	}

	public static boolean logError(String message) { // user log
		if(instructionBuffer != null) {
            instructionBuffer.addInstruction(new LogErrorInstruction(message));
			return true;
		}
		return false;
	}

	public static boolean runtimeLog(String message) {
		// log generated by the api package 
		// TODO not implemented
		return false;
	}
	
	public static boolean runtimeErrorLog(String message) {
		// error log generated by the api package 
		// TODO not implemented
		return false;
	}	
	
	static void setLocalInstanceToBeCreated(boolean bool) {
		localInstanceToBeCreated = bool;
	}
	
	public static boolean instructionImport() {
		return instructionBuffer != null;
	}
	
	static void importWarning(String msg) {
		System.out.println("Warning: " + msg);
	}
	
    private static CoreMethod instructionBuffer = null;
    private static boolean localInstanceToBeCreated = false;
    private static CoreModel currentModel = null;
}
