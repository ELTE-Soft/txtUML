package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;

public class TestSendActionModel extends Model {

	class testSig extends Signal{
		int val;
		String param;
	}
	
	class A extends ModelClass{
		
		class Init extends Initial{}
		
		class TestSendState extends State{
			@Override
			public void entry(){
	
				testSig testSig = new testSig();
				testSig.val = 1;
				testSig.param = "LOL";
				A cls = Action.create(A.class);
				
				Action.send(cls, testSig);
			}
		}
		
		@From(Init.class) @To(TestSendState.class)
		class InitToTestSendState extends Transition{}
	}
	
	class AToA extends Association{
		
		class thisEnd extends One<A>{}
		class thatEnd extends One<A>{}
		
	}

}
