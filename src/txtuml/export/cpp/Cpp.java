package txtuml.export.cpp;

import java.io.PrintWriter;
import java.io.IOException;
import txtuml.core.*;
import txtuml.importer.Importer;
import txtuml.importer.ImportException;

public class Cpp {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed.");
			return;
		}

		try {
			Model m = Importer.importModel(args[0]);
            String header = createHeader(m);
            PrintWriter writer = new PrintWriter(args[1] + "/" + m.getName() + ".hh", "UTF-8");
            writer.println(header);
            writer.close();
            String source = createSource(m);
            writer = new PrintWriter(args[1] + "/" + m.getName() + ".cpp", "UTF-8");
            writer.println(source);
            writer.close();
		} catch(ImportException ie) {
			System.out.println("Error: " + ie.getMessage());
		} catch(IOException ioe) {
			System.out.println("IO error.");
        }
	}
    
    static String createHeader(Model m) {
        String guard = "__" + m.getName() + "_HH__";
        return "#ifndef " + guard + "\n"
             + "#define " + guard + "\n\n"
             + "#include <string>\n"
             + "#include <vector>\n\n"
             + defineEvents(m) + "\n"
             + declareMethods(m) + "\n"
             + declareClasses(m) + "\n"
             + defineClasses(m)
             + "#endif // " + guard;
    }

    static String defineEvents(Model m) {
        String result = "enum event { ";
        boolean isFirst = true;
        for(txtuml.core.Event ev : m.getEvents()) {
        	if(!isFirst) {
        		result += ", ";
        		isFirst = false;
        	}
            result += ev.getName();
        }
        result += " };\n";
        return result;
    }

    static String declareMethods(Model m) {
        String result = "";
        for(txtuml.core.Method met : m.getMethods()) {
            result += "void " + met.getName() + "();\n";
        }
        return result;
    }

    static String declareClasses(Model m) {
        String result = "";
        for(txtuml.core.Class cl : m.getClasses()) {
            result += "struct " + cl.getName() + ";\n";
        }
        return result;
    }

    static String defineClasses(Model m) {
        String result = "";
        for(txtuml.core.Class cl : m.getClasses()) {
            result += "struct " + cl.getName() + "\n"
                    + "{\n"
                    + defineAttributes(cl,m)
                    + declareMethods(cl)
                    + defineStates(cl)
                    + "};\n\n";
        }
        return result;
    }
    
    static String defineAttributes(txtuml.core.Class cl, Model m) {
        String result = "";
        for(Attribute attr : cl.getAttributes()) {
            result += "  " + compileType(attr.getType()) + " " + attr.getName() + ";\n";
        }
        for(Association assoc : m.getAssociations()) {
        	if(assoc.getLeft().getParticipant() == cl) {
        		String name = assoc.getRight().getParticipant().getName();
        		if(assoc.getRight().getMultiplicity().equals(Multiplicity.One)) {
        			result += "  " + name + " *" + assoc.getRight().getPhrase() + ";\n";
        		} else {
        			result += "  std::vector<" + name + "*> " + assoc.getRight().getPhrase() + ";\n";
        		}
        	}
        	if(assoc.getRight().getParticipant() == cl) {
        		String name = assoc.getLeft().getParticipant().getName();
        		if(assoc.getLeft().getMultiplicity().equals(Multiplicity.One)) {
        			result += "  " + name + " *" + assoc.getLeft().getPhrase() + ";\n";
        		} else {
        			result += "  std::vector<*" + name + "*> " + assoc.getLeft().getPhrase() + ";\n";
        		}
        	}
        }
        return result;
    }

    static String declareMethods(txtuml.core.Class cl) {
        String result = "";
        for(Method m : cl.getMethods()) {
            result += "  void " + m.getName() + "();\n";
        }
        StateMachine stm = cl.getStateMachine();
        if(stm != null) {
        	for(State st : stm.getStates()) {
        		result += "  void " + st.getName() + "();\n";
        	}
        	for(Transition tr : stm.getTransitions()) {
        		result += "  void " + tr.getAction().getName() + "();\n";
        	}
        }
        return result;
    }

    static String defineStates(txtuml.core.Class cl) {
        String result = "";
        StateMachine stm = cl.getStateMachine();
        if(stm != null) {
            result += "  enum state { ";
            boolean first = true;
            for(State st : stm.getStates()) {
                if(!first) {
                    result += ", ";
                }
                result += "state_" + st.getName();
                first = false;
            }
            result += " };\n"
            		+ "  state current_state;\n"
            		+ "  " + cl.getName() + "();\n"
            		+ "  void send(event e);\n";
        }
        return result;
    }
    
    static String compileType(DataType dt) {
        switch(dt) {
            case IntType: return "int";
            case BoolType: return "bool";
            case StringType: return "std::string";
        }
        return "int";
    }
    
	static String createSource(Model m) {
        return "#include \"" + m.getName() + ".hh\"\n"
        	 + "#include <iostream>\n\n"
             + compileMethods(m)
             + compileMemberFunctions(m);
    }

	static String compileMethods(Model m) {
		String result = "";
		for(Method met : m.getMethods()) {
			result += "void " + met.getName() + "()\n"
			        + "{\n"
			        + compileInstructions(met)
			        + "}\n\n";
		}
		return result;
	}

	static String compileMemberFunctions(Model m) {
		String result = "";
		for(txtuml.core.Class cl : m.getClasses()) {
            result += compileMemberFunctionsOfClass(cl)
            		+ compileStates(cl)
            		+ compileTransitions(cl);
        }
		return result;
	}

	static String compileMemberFunctionsOfClass(txtuml.core.Class c) {
		String result = "";
		StateMachine stm = c.getStateMachine();
		if(stm != null) {
			result += c.getName() + "::" + c.getName() + "()\n"
					+ "{\n";
			State ini = stm.getInitialState(); 
			if(ini != null) {
				result += "  current_state = state_" + ini.getName() + ";\n";
			}
			result += "}\n\n";
			result += compileStateMachine(c,stm);
		}
		for(Method m : c.getMethods()) {
			result += "void " + c.getName() + "::" + m.getName() + "()\n"
					+ "{\n" + compileInstructions(m) + "}\n\n";
		}
		return result;
	}

	static String compileStates(txtuml.core.Class c) {
		String result = "";
		StateMachine stm = c.getStateMachine();
		if(stm != null) {
			for(State st : stm.getStates()) {
				result += "void " + c.getName() + "::" + st.getName() + "()\n"
						+ "{\n" + compileInstructions(st.getAction()) + "}\n\n";
			}
		}
		return result;
	}

	static String compileTransitions(txtuml.core.Class c) {
		String result = "";
		StateMachine stm = c.getStateMachine();
		if(stm != null) {
			for(Transition tr : stm.getTransitions()) {
				result += "void " + c.getName() + "::" + tr.getAction().getName() + "()\n"
						+ "{\n" + compileInstructions(tr.getAction()) + "}\n\n";
			}
		}
		return result;
	}

	static String compileInstructions(Method m) {
		String result = "";
		for(Instruction i : m.getInstructions()) {
			if(i instanceof CreateInstruction) {
				CreateInstruction ci = (CreateInstruction)i;
				String typeName = ci.getType().getName();
				String instName = ci.getReference().getName();
				result += "  " + typeName + " *" + instName + " = new " + typeName + "();\n";
			} else if(i instanceof LinkInstruction) {
				LinkInstruction li = (LinkInstruction)i;
				if(li.getAssociation().getRight().getMultiplicity().equals(Multiplicity.Many)) {
					result += "  " + li.getLeftInstance().getName() + "->"
				            + li.getAssociation().getRight().getPhrase()
				            + ".push_back(" + li.getRightInstance().getName() + ");\n";
				} else {
					result += "  " + li.getLeftInstance().getName() + "->"
				            + li.getAssociation().getRight().getPhrase()
				            + " = &" + li.getRightInstance().getName() + ";\n";
				}
				if(li.getAssociation().getLeft().getMultiplicity().equals(Multiplicity.Many)) {
					result += "  " + li.getRightInstance().getName() + "->"
				            + li.getAssociation().getLeft().getPhrase()
				            + ".push_back(" + li.getLeftInstance().getName() + ");\n";
				} else {
					result += "  " + li.getRightInstance().getName() + "->"
				            + li.getAssociation().getLeft().getPhrase()
				            + " = " + li.getLeftInstance().getName() + ";\n";
				}
			} else if(i instanceof CallInstruction) {
				CallInstruction ci = (CallInstruction)i;
				result += "  " + ci.getObject().getName() + "->"
				        + ci.getMethod().getName() + "();\n";
			} else if(i instanceof LogInstruction) {
				LogInstruction li = (LogInstruction)i;
				result += "  std::cout << \"" + li.getMessage() + "\" << std::endl;\n";
			} else if(i instanceof SelectOneInstruction) {
				SelectOneInstruction si = (SelectOneInstruction)i;
				String resultClassName;
				if(si.getAssociation().getRight().getPhrase().equals(si.getPhrase())) {
					// From left to right
					resultClassName = si.getAssociation().getRight().getParticipant().getName();
				} else {
					// From right to left
					resultClassName = si.getAssociation().getLeft().getParticipant().getName();
				}
				String startName;
				if(si.getStart().getName().equals(m.getSelf().getName())) {
					startName = "this";
				} else {
					startName = si.getStart().getName();
				}
				result += "  " + resultClassName + " *" + si.getResult().getName() + " = "
						+ startName + "->" + si.getPhrase() + ";\n";					
			} else if(i instanceof SendInstruction) {
				SendInstruction si = (SendInstruction)i;
				String receiverName;
				if(si.getReceiver().getName().equals(m.getSelf().getName())) {
					receiverName = "this";
				} else {
					receiverName = si.getReceiver().getName();
				}
				result += "  " + receiverName + "->send(" + si.getEvent().getName() + ");\n";
			}
		}
		return result;
	}
	
	static String compileStateMachine(txtuml.core.Class c, StateMachine stm) {
		String result = "";
		result += "void " + c.getName() + "::send(event e)\n" + "{\n";
		String branchKeyword = "if";
		for(Transition t : stm.getTransitions()) {
			result += "  " + branchKeyword + "(current_state == state_" + t.getFrom().getName()
					+ " && e == " + t.getTrigger().getName() + ")\n"
					+ "  {\n"
					+ "    " + t.getAction().getName() + "();\n"
					+ "    " + t.getTo().getName() + "();\n"
					+ "    current_state = state_" + t.getTo().getName() + ";\n"
					+ "  }\n";
			branchKeyword = "else if";
		}
		result += "}\n\n";

		return result;
	}
}


