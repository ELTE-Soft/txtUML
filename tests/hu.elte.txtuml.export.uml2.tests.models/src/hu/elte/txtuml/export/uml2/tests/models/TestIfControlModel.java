package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestIfControlModel extends Model {

	class TestModelClass extends ModelClass
	{		
		class Init extends Initial{}
		
		class IfControl extends State
		{
			@Override
			public void entry()
			{	
				int condInt = 1;
				
				TestModelClass cls = null;
				
				if(condInt == 1)
				{
					cls = Action.create(TestModelClass.class, 1);
				}
				else
				{
					cls = Action.create(TestModelClass.class, "testing");
				}
				
				Action.delete(cls);
			}
		}
		
		@From(Init.class) @To(IfControl.class)
		class InitIfControl extends Transition {}
	}

}
