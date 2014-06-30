package txtuml.importer;

import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import txtuml.core.*;
import txtuml.core.instructions.*;
import txtuml.api.Association;
import txtuml.api.Initial;
import txtuml.api.ModelClass;
import txtuml.api.ModelObject;
import txtuml.api.ModelType;
import txtuml.api.ModelInt;
import txtuml.api.ModelBool;
import txtuml.api.ModelString;
import txtuml.api.Operation;
import txtuml.api.State;
import txtuml.api.Transition;
import txtuml.api.Event;
import txtuml.utils.InstanceCreator;

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
                model.addEvent(new CoreEvent(c.getSimpleName()));
			}
		}
        // Import method names
        for(Method m : modelClass.getDeclaredMethods()) {
            if(m.getParameterTypes().length == 0) { // TODO remove when parameters are handled
                model.addMethodName(m.getName());
            }
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
                importMethod(coreMethod,m,modelClass,model);
			}
		}
        // Import class method bodies
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                CoreClass coreClass = model.getClass(c.getSimpleName());
				for(Method m : c.getDeclaredMethods()) {
                    CoreMethod coreMethod = coreClass.getMethod(m.getName());
                    if(coreMethod != null) {
                        importMethod(coreMethod,m,c,model);
                    }
                }
			}
		}
        // Import class state machines
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                CoreClass coreClass = model.getClass(c.getSimpleName());
                coreClass.setStateMachine(importStateMachine(coreClass,c,model));
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
			return Class.forName(className);
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }
	
	static boolean isClass(Class<?> c) {
		return (c.getSuperclass() == ModelClass.class);
		// TODO not allow two or more level inheritance from ModelClass
	}

	static boolean isAssociation(Class<?> c) {
		return (c.getSuperclass() == Association.class);
		// TODO not allow two or more level inheritance from Association		
	}
    
	static boolean isEvent(Class<?> c) {
		return (c.getSuperclass() == Event.class);
		// TODO not allow two or more level inheritance from Event
	}

    static boolean isAttribute(Field f) {
        return f.getType().getSuperclass() == ModelType.class;   
    }
    
    static boolean isState(Method m) {
        return m.isAnnotationPresent(State.class);
    }

    static boolean isInitialState(Method m) {
    	return m.isAnnotationPresent(Initial.class);
    }
    
    static boolean isTransition(Method m) {
        return m.isAnnotationPresent(Transition.class);
    }

    static boolean isOperation(Method m) {
    	return m.isAnnotationPresent(Operation.class);
    }
    
    static boolean isMemberFunction(Method m) {
        return (m.getDeclaredAnnotations().length == 0
        		|| (m.getDeclaredAnnotations().length == 1 && m.isAnnotationPresent(Operation.class) ) )
               && m.getParameterTypes().length == 0; // TODO remove when parameters are handled
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
                             Class<?> declaringClass, CoreModel model, Object... params) {
		instructionBuffer = coreMethod;
		sourceMethod.setAccessible(true);
        setLocalInstanceToBeDeclared(true);
		Object o = InstanceCreator.createInstance(declaringClass,3);
        setLocalInstanceToBeDeclared(false);
        if(o instanceof ModelClass) {
            coreMethod.setSelf(new CoreInstance(((ModelClass)o).getIdentifier()));
            InstanceCreator.createInstance(ModelObject.class, 1, o);
        }
		try {
			sourceMethod.invoke(o,params);
		} catch(Exception e) {
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
    
    static CoreStateMachine importStateMachine(CoreClass coreClass,
      Class<?> sourceClass, CoreModel model) throws ImportException {
        CoreStateMachine result = new CoreStateMachine();
        for(Method method : sourceClass.getDeclaredMethods()) {
			boolean isState = false, isTransition = false;
            if(isState(method)) {
            	isState = true;
            	CoreState st = importState(method,coreClass,sourceClass,model);
                result.addState(st);
                if(isInitialState(method)) {
        			if (result.getInitialState() != null) {
                    	throw new ImportException(sourceClass.getName() + " has two initial states: " + result.getInitialState().getName() + ", " + method.getName());
        			}
                	result.setInitialState(st);
                }
            }
            if(isTransition(method)) {
            	isTransition = true;
				if (isState) {
					throw new ImportException(sourceClass.getName() + "." + method.getName() + " cannot be a state and a transition at the same time");
				}
                result.addTransition(importTransition(method,result,sourceClass,model));
            }
            if (isOperation(method)) {
				if (isState) {
					throw new ImportException(sourceClass.getName() + "." + method.getName() + " cannot be a state and an operation at the same time");
				}
				if (isTransition) {
					throw new ImportException(sourceClass.getName() + "." + method.getName() + " cannot be a transition and operation at the same time");
				}
        	} else if (!isState && !isTransition) {
    			importWarning(sourceClass.getName() + "." + method.getName() + " is a non-state method not annotated to be an operation");				
        	}
        }
        if(result.getStates().size() != 0 && result.getInitialState() == null) {
        	importWarning(sourceClass.getName() + " has one or more states but no initial state (state machine will not be created)");
        	return null;
        }
        return result;
    }
    
    static CoreState importState(Method state, CoreClass coreClass, Class<?> sourceClass, CoreModel model) {
		String stateName = state.getName();
        CoreMethod coreMethod = new CoreMethod(stateName);
        importMethod(coreMethod, state, sourceClass, model);
        return new CoreState(stateName, coreMethod);
    }

    static CoreTransition importTransition(Method trans, CoreStateMachine stateMachine, Class<?> sourceClass, CoreModel model) {
		String trName = trans.getName();
        Transition trAnnot = (Transition)trans.getAnnotation(Transition.class);
        CoreState from = stateMachine.getState(trAnnot.from());
        CoreState to = stateMachine.getState(trAnnot.to());
        CoreEvent event = model.getEvent(trans.getParameterTypes()[0].getSimpleName());
        CoreMethod coreMethod = new CoreMethod(trName);
        importMethod(coreMethod, trans, sourceClass, model, (Object)null);
        return new CoreTransition(event, from, to, coreMethod);
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
    
    public static boolean call(ModelClass obj, String methodName) {
        if(instructionBuffer != null) {
            CoreInstance o = new CoreInstance(obj.getIdentifier());
            CoreClass c = currentModel.getClass(obj.getClass().getSimpleName());
            CoreMethod m = c.getMethod(methodName);
            instructionBuffer.addInstruction(new CallInstruction(o,m));
            return true;
        }
        return false;
    }

	public static ModelClass selectOne(ModelClass start, Class<? extends Association> assocClass, String phrase) {
        if(instructionBuffer != null) {
            Field[] fields = assocClass.getDeclaredFields();
            ModelClass result = null;
			for(Field field : fields) {
				if(field.getName().equals(phrase)) {
                    setLocalInstanceToBeDeclared(true);
					result = (ModelClass)InstanceCreator.createInstance(field.getType(),3);
		            InstanceCreator.createInstance(ModelObject.class, 1, result);
                    setLocalInstanceToBeDeclared(false);
                    break;
				}
			}            
            CoreInstance st = new CoreInstance(start.getIdentifier());
            CoreAssociation asc = currentModel.getAssociation(assocClass.getSimpleName());
            CoreInstance res = new CoreInstance(result.getIdentifier());
            instructionBuffer.addInstruction(new SelectOneInstruction(st,asc,phrase,res));
            return result;
        }
        return null;
	}

	public static boolean send(ModelClass receiver, Class<? extends Event> eventClass) {
		if(instructionBuffer != null) {
            CoreEvent ev = currentModel.getEvent(eventClass.getSimpleName());
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
		// TODO Auto-generated method stub
		return false;
	}
	
	public static boolean runtimeErrorLog(String message) {
		// TODO Auto-generated method stub
		return false;
	}	
	
	public static boolean callExternal(Class<?> c, String methodName) {
		if(instructionBuffer != null) {
            // TODO not implemented 
			// instructionBuffer.addInstruction(new CallExternalInstruction(c,methodName));
			return true;
		}
		return false;
	}

	static void setLocalInstanceToBeDeclared(boolean bool) {
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
