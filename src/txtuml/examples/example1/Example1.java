package txtuml.examples.example1;

import txtuml.api.*;

class Model1 {
	
	class Machine extends ModelClass {
		ModelInt modelNumber;
		void init() {
		}
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
		ModelString name;
		void doWork() {
			Action.log("Staring to work...");
			Machine myMachine = (Machine)Action.selectOne(this, new Usage(), "usedMachine");
			Action.send(new ButtonPress(), myMachine);
			Action.send(new ButtonPress(), myMachine);
            Action.log("Work finished...");
		}
	}
    
	class Usage extends Association {
		@One Machine usedMachine;
		@Many User userOfMachine;
	}

	class ButtonPress extends Event {}

	public void test() {
        Machine m = new Machine();
		User u1 = new User();
        Action.link(Usage.class, "usedMachine", m, "userOfMachine", u1);
		User u2 = new User();
        Action.link(Usage.class, "usedMachine", m, "userOfMachine", u2);
        Action.call(u1, "doWork");
        Action.call(u2, "doWork");
	}
}

public class Example1 {
	public static void main(String[] args) {
		Model1 mod = new Model1();
		mod.test();
	}
}
