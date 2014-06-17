package txtuml.importer;

import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import txtuml.core.Model;
import txtuml.core.Association;
import txtuml.core.AssociationEnd;
import txtuml.core.Attribute;
import txtuml.core.DataType;
import txtuml.core.Multiplicity;
import txtuml.core.Instance;
import txtuml.core.CreateInstruction;
import txtuml.core.LogInstruction;
import txtuml.core.LinkInstruction;
import txtuml.core.CallInstruction;
import txtuml.core.SelectOneInstruction;
import txtuml.core.SendInstruction;
import txtuml.core.Event;
import txtuml.api.Initial;
import txtuml.api.ModelClass;
import txtuml.api.ModelType;
import txtuml.api.ModelInt;
import txtuml.api.ModelBool;
import txtuml.api.ModelString;
import txtuml.api.State;
import txtuml.api.Transition;
import txtuml.utils.InstanceCreator;

public class Importer {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("One command line argument needed.");
			return;
		}

		try {
			Model m = importModel(args[0]);
		} catch(ImportException ie) {
			System.out.println("Error: " + ie.getMessage());
		}
	}

	public static Model importModel(String className) throws ImportException {
		Class modelClass = findModel(className);
		
		Model model = new Model(modelClass.getSimpleName());
		
        currentModel = model;
        // Import class names
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
				model.addClassName(c.getSimpleName());
			}
		}
        // Import associations
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isAssociation(c)) {
                model.addAssociation(importAssociation(c,model));
			}
		}
        // Import events
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isEvent(c)) {
                model.addEvent(new Event(c.getSimpleName()));
			}
		}
        // Import method names
        for(Method m : modelClass.getDeclaredMethods()) {
            if(m.getParameterTypes().length == 0) { // TODO: remove when parameters are handled
                model.addMethodName(m.getName());
            }
        }
        // Import class attributes
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                txtuml.core.Class coreClass = model.getClass(c.getSimpleName());
				for(Field f : c.getDeclaredFields()) {
                    if(isAttribute(f)) {
                        coreClass.addAttribute(importAttribute(f));
                    }
                }
			}
		}
        // Import member function names
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                txtuml.core.Class coreClass = model.getClass(c.getSimpleName());
				for(Method m : c.getDeclaredMethods()) {
                    if(isMemberFunction(m)) {
                        coreClass.addMethodName(m.getName());
                    }
                }
			}
		}
        // Import method bodies
		for(Method m : modelClass.getDeclaredMethods()) {
			txtuml.core.Method coreMethod = model.getMethod(m.getName());
            if(coreMethod != null) {
                importMethod(coreMethod,m,modelClass,model);
			}
		}
        // Import class method bodies
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
                txtuml.core.Class coreClass = model.getClass(c.getSimpleName());
				for(Method m : c.getDeclaredMethods()) {
                    txtuml.core.Method coreMethod = coreClass.getMethod(m.getName());
                    if(coreMethod != null) {
                        importMethod(coreMethod,m,c,model);
                    }
                }
			}
		}
        // Import class state machines
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isClass(c) && hasStateMachine(c)) {
                txtuml.core.Class coreClass = model.getClass(c.getSimpleName());
                coreClass.setStateMachine(importStateMachine(coreClass,c,model));
			}
		}
        currentModel = null;
		return model;
	}

    static txtuml.core.Class importClass() {
        return null;
    }

    static Class findModel(String className) throws ImportException {
		try {
			return Class.forName(className);
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }
	
	static boolean isClass(Class c) {
		return (c.getSuperclass() == ModelClass.class);
	}

	static boolean isAssociation(Class c) {
		return (c.getSuperclass() == txtuml.api.Association.class);
	}
    
	static boolean isEvent(Class c) {
		return (c.getSuperclass() == txtuml.api.Event.class);
	}

    static boolean isAttribute(Field f) {
        return f.getType().getSuperclass() == ModelType.class;            
    }
        
    static boolean isState(Method m) {
        return m.getAnnotation(State.class) != null;
    }

    static boolean isInitialState(Method m) {
    	return m.getAnnotation(Initial.class) != null;
    }
    
    static boolean isTransition(Method m) {
        return m.getAnnotation(Transition.class) != null;
    }

    static boolean isMemberFunction(Method m) {
        return m.getDeclaredAnnotations().length == 0
               && m.getParameterTypes().length == 0; // TODO: remove when parameters are handled
    }

    static boolean hasStateMachine(Class c) {
        for(Method m : c.getDeclaredMethods()) {
            if(isState(m)) {
                return true;
            }
        }
        return false;
    }
    
    static Association importAssociation(Class c, Model m) throws ImportException {
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
        return new Association(c.getSimpleName(),
                               importAssociationEnd(fs.get(0),m),
                               importAssociationEnd(fs.get(1),m));
    }

    static AssociationEnd importAssociationEnd(Field f, Model m) throws ImportException {
        String phrase = f.getName();
        String className = f.getType().getSimpleName();
        txtuml.core.Class participant = m.getClass(className);
        if(participant == null) {
            throw new ImportException(phrase + ": No class " + className + "found in this model.");
        }
        Annotation[] as = f.getDeclaredAnnotations();
        if(as.length != 1) {
            throw new ImportException(phrase + ": cannot determine multiplicity. (Number of annotations must be 1, found "
                                             + Integer.toString(as.length) + ".)");
        }
        Multiplicity multiplicity;
        if(as[0].annotationType().equals(txtuml.api.One.class)) {
            multiplicity = Multiplicity.One;
        } else if(as[0].annotationType().equals(txtuml.api.Many.class)) {
            multiplicity = Multiplicity.Many;
        } else {
            throw new ImportException(phrase + ": has invalid multiplicity.");            
        }
        return new AssociationEnd(participant,phrase,multiplicity);
    }

	static void importMethod(txtuml.core.Method coreMethod, Method sourceMethod,
                             Class declaringClass, Model model, Object... params) {
		instructionBuffer = coreMethod;
		sourceMethod.setAccessible(true);
        setLocalInstanceToBeDeclared(true);
		Object o = InstanceCreator.createInstance(declaringClass,3);
        setLocalInstanceToBeDeclared(false);
        if(o instanceof ModelClass) {
            coreMethod.setSelf(new Instance(((ModelClass)o).getIdentifier()));
        } else {
        }
		try {
			sourceMethod.invoke(o,params);
		} catch(Exception e) {
			e.printStackTrace();
		}
		instructionBuffer = null;
	}

    static Attribute importAttribute(Field f) {
        return new Attribute(f.getName(), importType(f.getType()));
    }
    
    static DataType importType(Class t) {
        if(t == ModelInt.class) {
            return DataType.IntType;
        }
        if(t == ModelBool.class) {
            return DataType.BoolType;
        }
        if(t == ModelString.class) {
            return DataType.StringType;
        }
        return null;
    }
    
    static txtuml.core.StateMachine importStateMachine(txtuml.core.Class coreClass,
      Class sourceClass, Model model) throws ImportException {
        txtuml.core.StateMachine result = new txtuml.core.StateMachine();
        for(Method m : sourceClass.getDeclaredMethods()) {
            if(isState(m)) {
            	txtuml.core.State st = importState(m,coreClass,sourceClass,model);
                result.addState(st);
                if(isInitialState(m)) {
                	result.setInitialState(st);
                }
            }
        }
        for(Method m : sourceClass.getDeclaredMethods()) {
            if(isTransition(m)) {
                result.addTransition(importTransition(m,result,sourceClass,model));
            }
        }
        return result;
    }
    
    static txtuml.core.State importState(Method state, txtuml.core.Class coreClass, Class sourceClass, Model model) {
		String stateName = state.getName();
        txtuml.core.Method coreMethod = new txtuml.core.Method(stateName);
        importMethod(coreMethod, state, sourceClass, model);
        return new txtuml.core.State(stateName, coreMethod);
    }

    static txtuml.core.Transition importTransition(Method trans, txtuml.core.StateMachine stateMachine, Class sourceClass, Model model) {
		String trName = trans.getName();
        Transition trAnnot = (Transition)trans.getAnnotation(Transition.class);
        txtuml.core.State from = stateMachine.getState(trAnnot.from());
        txtuml.core.State to = stateMachine.getState(trAnnot.to());
        txtuml.core.Event event = model.getEvent(trans.getParameterTypes()[0].getSimpleName());
        txtuml.core.Method coreMethod = new txtuml.core.Method(trName);
        importMethod(coreMethod, trans, sourceClass, model, (Object)null);
        return new txtuml.core.Transition(event, from, to, coreMethod);
    }
        
    public static void createInstance(ModelClass created) {
        if(instructionBuffer != null) {
            if(!localInstanceToBeCreated) {
                txtuml.core.Class type = currentModel.getClass(created.getClass().getSimpleName());
                Instance ref = new Instance(created.getIdentifier());
                instructionBuffer.addInstruction(new CreateInstruction(type,ref));
			}
        }
    }
    
    public static boolean link(Class assoc, String leftPhrase, ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        if(instructionBuffer != null) {
            Instance left = new Instance(leftObj.getIdentifier());
            Instance right = new Instance(rightObj.getIdentifier());
            Association asc = currentModel.getAssociation(assoc.getSimpleName());
            instructionBuffer.addInstruction(new LinkInstruction(left,right,asc));
            return true;
        }
        return false;
    }
    
    public static boolean call(ModelClass obj, String methodName) {
        if(instructionBuffer != null) {
            Instance o = new Instance(obj.getIdentifier());
            txtuml.core.Class c = currentModel.getClass(obj.getClass().getSimpleName());
            txtuml.core.Method m = c.getMethod(methodName);
            instructionBuffer.addInstruction(new CallInstruction(o,m));
            return true;
        }
        return false;
    }

	public static ModelClass selectOne(ModelClass start, txtuml.api.Association assoc, String phrase) {
        if(instructionBuffer != null) {
            Field[] fields = assoc.getClass().getDeclaredFields();
            ModelClass result = null;
			for(Field field : fields) {
				if(field.getName().equals(phrase)) {
                    setLocalInstanceToBeDeclared(true);
					result = (ModelClass)InstanceCreator.createInstance(field.getType(),3);
                    setLocalInstanceToBeDeclared(false);
                    break;
				}
			}            
            Instance st = new Instance(start.getIdentifier());
            txtuml.core.Association asc = currentModel.getAssociation(assoc.getClass().getSimpleName());
            Instance res = new Instance(result.getIdentifier());
            instructionBuffer.addInstruction(new SelectOneInstruction(st,asc,phrase,res));
            return result;
        }
        return null;
	}
    
	public static boolean send(Object event, ModelClass receiver) {
		if(instructionBuffer != null) {
            txtuml.core.Event ev = currentModel.getEvent(event.getClass().getSimpleName());
            Instance rec = new Instance(receiver.getIdentifier());
            instructionBuffer.addInstruction(new SendInstruction(ev,rec));
			return true;
		}
		return false;
	}

	public static boolean log(String message) {
		if(instructionBuffer != null) {
            instructionBuffer.addInstruction(new LogInstruction(message));
			return true;
		}
		return false;
	}

	public static boolean callExternal(Class c, String methodName) {
		if(instructionBuffer != null) {
            // TODO: instructionBuffer.addInstruction(new CallExternalInstruction(c,methodName));
			return true;
		}
		return false;
	}

	static void setLocalInstanceToBeDeclared(boolean b) {
		localInstanceToBeCreated = b;
	}
	
	public static boolean instructionImport() {
		return instructionBuffer != null;
	}
	
    static txtuml.core.Method instructionBuffer = null;
    static boolean localInstanceToBeCreated = false;
    static txtuml.core.Model currentModel = null;
}
