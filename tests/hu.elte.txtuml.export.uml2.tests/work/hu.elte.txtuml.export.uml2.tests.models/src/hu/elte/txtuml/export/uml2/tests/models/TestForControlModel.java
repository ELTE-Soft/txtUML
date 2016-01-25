package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestForControlModel extends Model {
	class TestModelClass extends ModelClass
	{		
		class Init extends Initial{}
		
		class ForControl extends State
		{
			@Override
			public void entry()
			{	
				int condInt = 5;
				
				TestModelClass[] cls = new TestModelClass[5];
				
				for( int i = 0; i < condInt; ++i)
				{
					cls[i] = Action.create(TestModelClass.class, i);
				}
				
				for( int i = 0 ; i < condInt; ++i)
				{
					Action.delete(cls[i]);
				}
			}
		}
		
		@From(Init.class) @To(ForControl.class)
		class InitForControl extends Transition {}
	}

}
