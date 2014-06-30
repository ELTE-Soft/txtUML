package txtuml.examples.example1;

import txtuml.api.*;

class Model1 {
	
	class Machine extends ModelClass {
		ModelInt modelNumber = new ModelInt(200);
        @State void on() {
            Action.log("Current state: 'on'");
            Action.callExternal(txtuml.examples.example1.MachineUI.class, "showOn");
		}
		@Initial @State void off() {
            Action.log("Current state: 'off'");
		}
		@Transition(from="off", to="on")
        void switchOn(ButtonPress event) {
			Action.log("Switch on...");
		}
		@Transition(from="on", to="off")
        void switchOff(ButtonPress event) {
			Action.log("Switch off...");
		}
	}
	
	class User extends ModelClass {
		ModelString name = new ModelString(); // equivalent to new ModelString(""); 
		@Operation
		void doWork() {
			Action.log("Starting to work...");
			ModelObject<Machine> myMachine = // type-safe usage
					Action.selectOne(self(), Usage.class, "usedMachine");
					// using "this" does not work, must use protected function self() (inherited from ModelClass)
			Action.send(myMachine, ButtonPress.class);
			Action.send(myMachine, ButtonPress.class);
            Action.log("Work finished...");
		}
	}
    
	class Usage extends Association {
		@One Machine usedMachine;
		@Many User userOfMachine;
	}

	class ButtonPress extends Event {}

	public void test() {		
		//three ways to use class ModelObject
		@SuppressWarnings("rawtypes")
		ModelObject m = // simplest version (with warning)
				Action.create(Machine.class); 
		ModelObject<?> u1 = // without warning
				Action.create(User.class);
		ModelObject<User> u2 = // type-safe usage
				Action.create(User.class);
		
		//all the three works correctly with the methods of class Action 
        Action.link(Usage.class, "usedMachine", m, "userOfMachine", u1);
        Action.link(Usage.class, "usedMachine", m, "userOfMachine", u2);
        Action.call(u1, "doWork");
        Action.call(u2, "doWork");
        
        /*
         * to test the instance deletion
        
        Action.unLink(Usage.class, "usedMachine", m, "userOfMachine", u1); // must delete all the links to an instance before deleting it
        Action.unLink(Usage.class, "usedMachine", m, "userOfMachine", u2);
        Action.delete(m); // problem here: the machine object contained in m does not finish processing its four events before being deleted
        Action.log("Machine instance deleted");
        
        */
   	}
}

public class Example1 {
	public static void main(String[] args) {
		Model1 mod = new Model1();
		mod.test();
	}
}