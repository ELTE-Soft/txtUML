package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestStartActionModel extends Model {

	class A extends ModelClass{
		class Init extends Initial{
			
		}
		
		class StartStateTest extends State{
			@Override
			public void entry()
			{
				A cls = Action.create(A.class);
				
				Action.start(cls);
			}
		}
		
		@From(Init.class) @To(StartStateTest.class)
		class InitToStartState extends Transition{
			
		}
	}

}
