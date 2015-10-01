package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestCreateAndDestroyActionModel extends Model {

	class TestModelClass extends ModelClass
	{		
		class Init extends Initial{}
		
		class ObjectCreate extends State
		{
			@Override
			public void entry()
			{
				TestModelClass cls = Action.create(TestModelClass.class, 1, 2);
				Action.delete(cls);
			}
		}
		
		@From(Init.class) @To(ObjectCreate.class)
		class InitObjectCreate extends Transition {}
	}
}
