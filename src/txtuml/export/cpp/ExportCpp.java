package txtuml.export.cpp;

import java.io.PrintWriter;
import java.io.IOException;

import txtuml.core.*;
import txtuml.core.instructions.*;
import txtuml.importer.Importer;
import txtuml.importer.ImportException;

public class ExportCpp {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed.");
			return;
		}

		try {
			CoreModel m = Importer.importModel(args[0]);
            String header = createHeader(m);
            PrintWriter writer = new PrintWriter(args[1] + "\\" + m.getName() + ".hh", "UTF-8");
            writer.println(header);
            writer.close();
            String source = createSource(m);
            writer = new PrintWriter(args[1] + "\\" + m.getName() + ".cpp", "UTF-8");
            writer.println(source);
            writer.close();
		} catch(ImportException ie) {
			System.out.println("Error: " + ie.getMessage());
		} catch(IOException ioe) {
			System.out.println("IO error.");
        }
	}
    
    static String createHeader(CoreModel m) {
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

    static String defineEvents(CoreModel m) {
        String result = "enum event { ";
        boolean isFirst = true;
        for(CoreSignal ev : m.getEvents()) {
        	if(!isFirst) {
        		result += ", ";
        	}
            result += ev.getName();
    		isFirst = false;
        }
        result += " };\n";
        return result;
    }

    static String declareMethods(CoreModel m) {
        String result = "";
        for(CoreMethod met : m.getMethods()) {
            result += "void " + met.getName() + "();\n";
        }
        return result;
    }

    static String declareClasses(CoreModel m) {
        String result = "";
        for(CoreClass cl : m.getClasses()) {
            result += "struct " + cl.getName() + ";\n";
        }
        return result;
    }

    static String defineClasses(CoreModel m) {
        String result = "";
        for(CoreClass cl : m.getClasses()) {
            result += "struct " + cl.getName() + "\n"
                    + "{\n"
                    + defineAttributes(cl,m)
                    + declareMethods(cl)
                    + defineStates(cl)
                    + "};\n\n";
        }
        return result;
    }
    
    static String defineAttributes(CoreClass cl, CoreModel m) {
        String result = "";
        for(CoreAttribute attr : cl.getAttributes()) {
            result += "  " + compileType(attr.getType()) + " " + attr.getName() + ";\n";
        }
        for(CoreAssociation assoc : m.getAssociations()) {
        	if(assoc.getLeft().getParticipant() == cl) {
        		String name = assoc.getRight().getParticipant().getName();
        		if(assoc.getRight().getMultiplicity().equals(CoreMultiplicity.One) || assoc.getRight().getMultiplicity().equals(CoreMultiplicity.MaybeOne)) {
        			result += "  " + name + " *" + assoc.getRight().getPhrase() + ";\n";
        		} else {
        			result += "  std::vector<" + name + "*> " + assoc.getRight().getPhrase() + ";\n";
        		}
        	}
        	if(assoc.getRight().getParticipant() == cl) {
        		String name = assoc.getLeft().getParticipant().getName();
        		if(assoc.getLeft().getMultiplicity().equals(CoreMultiplicity.One) || assoc.getRight().getMultiplicity().equals(CoreMultiplicity.MaybeOne)) {
        			result += "  " + name + " *" + assoc.getLeft().getPhrase() + ";\n";
        		} else {
        			result += "  std::vector<*" + name + "*> " + assoc.getLeft().getPhrase() + ";\n";
        		}
        	}
        }
        return result;
    }

    static String declareMethods(CoreClass cl) {
        String result = "";
        for(CoreMethod m : cl.getMethods()) {
            result += "  void " + m.getName() + "();\n";
        }
        CoreStateMachine stm = cl.getStateMachine();
        if(stm != null) {
        	for(CoreState st : stm.getStates()) {
        		if (!st.isInitial()) {
		    		result += "  void " + st.getName() + "_entry();\n";
		    		result += "  void " + st.getName() + "_exit();\n";
        		}
        	}
        	for(CoreTransition tr : stm.getTransitions()) {
        		if (tr.getEffect() != null) 
        			result += "  void " + tr.getEffect().getName() + "();\n";
        		if (tr.getGuard() != null) 
        			result += "  bool " + tr.getGuard().getName() + "();\n";
        	}
        }
        return result;
    }

    static String defineStates(CoreClass cl) {
        String result = "";
        CoreStateMachine stm = cl.getStateMachine();
        if(stm != null) {
            result += "  enum state { ";
            boolean first = true;
            for(CoreState st : stm.getStates()) {
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
    
    static String compileType(CoreDataType dt) {
        switch(dt) {
            case IntType: return "int";
            case BoolType: return "bool";
            case StringType: return "std::string";
        }
        return "int";
    }
    
	static String createSource(CoreModel m) {
        return "#include \"" + m.getName() + ".hh\"\n"
        	 + "#include <iostream>\n\n"
             + compileMethods(m)
             + compileMemberFunctions(m);
    }

	static String compileMethods(CoreModel m) {
		String result = "";
		for(CoreMethod met : m.getMethods()) {
			result += "void " + met.getName() + "()\n"
			        + "{\n"
			        + compileInstructions(met)
			        + "}\n\n";
		}
		return result;
	}

	static String compileMemberFunctions(CoreModel m) {
		String result = "";
		for(CoreClass cl : m.getClasses()) {
            result += compileMemberFunctionsOfClass(cl)
            		+ compileStates(cl)
            		+ compileTransitions(cl);
        }
		return result;
	}

	static String compileMemberFunctionsOfClass(CoreClass c) {
		String result = "";
		CoreStateMachine stm = c.getStateMachine();
		if(stm != null) {
			result += c.getName() + "::" + c.getName() + "()\n"
					+ "{\n";
			CoreState ini = stm.getInitialState(); 
			if(ini != null) {
				result += "  current_state = state_" + ini.getName() + ";\n";
				for (CoreTransition tr : c.getStateMachine().getTransitions()) {
					if (tr.getFrom() == ini) {
						if (tr.getEffect() != null) {
							result += "  " + tr.getEffect().getName() + "();\n";
						}
						result += "  current_state = state_" + tr.getTo().getName() + ";\n";
						break;
					}
				}
			}
			result += "}\n\n";
			result += compileStateMachine(c,stm);
		}
		for(CoreMethod m : c.getMethods()) {
			result += "void " + c.getName() + "::" + m.getName() + "()\n"
					+ "{\n" + compileInstructions(m) + "}\n\n";
		}
		return result;
	}

	static String compileStates(CoreClass c) {
		String result = "";
		CoreStateMachine stm = c.getStateMachine();
		if(stm != null) {
			for(CoreState st : stm.getStates()) {
				if (!st.isInitial()) {
					result += "void " + c.getName() + "::" + st.getName() + "_entry()\n"
							+ "{\n" + compileInstructions(st.getEntry()) + "}\n\n";
					result += "void " + c.getName() + "::" + st.getName() + "_exit()\n"
							+ "{\n" + compileInstructions(st.getExit()) + "}\n\n";
				}
			}
		}
		return result;
	}

	static String compileTransitions(CoreClass c) {
		String result = "";
		CoreStateMachine stm = c.getStateMachine();
		if(stm != null) {
			for(CoreTransition tr : stm.getTransitions()) {
				if (tr.getEffect() != null) {
					result += "void " + c.getName() + "::" + tr.getEffect().getName() + "()\n"
							+ "{\n" + compileInstructions(tr.getEffect()) + "}\n\n";
				}
				if (tr.getGuard() != null) {
					result += "bool " + c.getName() + "::" + tr.getGuard().getName() + "()\n"
							+ "{\n" + compileInstructions(tr.getGuard()) + " return true;" // TODO needs to return the right value (when return values are handled)
									+ "}\n\n";
				}
			}
		}
		return result;
	}

	static String compileInstructions(CoreMethod m) {
		String result = "";
		for(Instruction i : m.getInstructions()) {
			if(i instanceof CreateInstruction) {
				CreateInstruction ci = (CreateInstruction)i;
				String typeName = ci.getType().getName();
				String instName = ci.getReference().getName();
				result += "  " + typeName + " *" + instName + " = new " + typeName + "();\n";
			} else if(i instanceof DeleteInstruction) {
				DeleteInstruction di = (DeleteInstruction)i;
				String instName = di.getReference().getName();
				result += "  delete " + instName + ";\n";
			} else if(i instanceof LinkInstruction) {
				LinkInstruction li = (LinkInstruction)i;
				if(li.getAssociation().getRight().getMultiplicity().equals(CoreMultiplicity.Many) || li.getAssociation().getRight().getMultiplicity().equals(CoreMultiplicity.Some)) {
					result += "  " + li.getLeftInstance().getName() + "->"
				            + li.getAssociation().getRight().getPhrase()
				            + ".push_back(" + li.getRightInstance().getName() + ");\n";
				} else {
					result += "  " + li.getLeftInstance().getName() + "->"
				            + li.getAssociation().getRight().getPhrase()
				            + " = &" + li.getRightInstance().getName() + ";\n";
				}
				if(li.getAssociation().getLeft().getMultiplicity().equals(CoreMultiplicity.Many) || li.getAssociation().getLeft().getMultiplicity().equals(CoreMultiplicity.Some)) {
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
			} else if(i instanceof LogErrorInstruction) {
				LogErrorInstruction eli = (LogErrorInstruction)i;
				result += "  std::cerr << \"" + eli.getMessage() + "\" << std::endl;\n";
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
				if(m.getSelf() != null && si.getStart().getName().equals(m.getSelf().getName())) {
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
			// TODO create the implementation of UnLinkInstruction
		}
		return result;
	}
	
	static String compileStateMachine(CoreClass c, CoreStateMachine stm) {
		// TODO create the implementation of composite states
		String result = "";
		result += "void " + c.getName() + "::send(event e)\n" + "{\n";
		String branchKeyword = "if";
		for(CoreTransition t : stm.getTransitions()) {
			if (t.getTrigger() != null) {
				String guard = "";
				if (t.getGuard() != null) {
					guard = " && " + t.getGuard().getName() + "()";
				}
				result += "  " + branchKeyword + "(current_state == state_" + t.getFrom().getName()
						+ " && e == " + t.getTrigger().getName() + guard + ")\n"
						+ "  {\n"
						+ "    " + t.getFrom().getName() + "_exit();\n"
						+ "    " + t.getEffect().getName() + "();\n"
						+ "    current_state = state_" + t.getTo().getName() + ";\n"
						+ "    " + t.getTo().getName() + "_entry();\n"
						+ "  }\n";
				branchKeyword = "else if";
			} else {
				
			}
		}
		result += "}\n\n";

		return result;
	}
}


