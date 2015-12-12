package hu.elte.txtuml.examples.validation;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class StateMethodNotCorrect extends ModelClass {

	public class Init extends Initial {
		public void action() {
		}
	}

	public class St extends State {
		public void entry() {
		}
		
		public void exit(int x, int y) {
		}
		
		public void foo(int x) {
			
		}
	}

	@From(Init.class)
	@To(St.class)
	public class Tr extends Transition {
		public void effect() {
			
		}
		
		public boolean guard() {
			return true;
		}
	}

}
