package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestLinkAndUnlinkActionModel extends Model {

	class A extends ModelClass
	{
		protected A connnectionVal;
		
		class Init extends Initial{}
		
		class LinkUnlinkAction extends State{
			
			@Override
			public void entry()
			{
				A cls = Action.create(A.class);
				A cls2 = Action.create(A.class);
				
				Action.link(AToA.ThisEnd.class, cls, AToA.OtherEnd.class , cls2);
				
				Action.unlink(AToA.ThisEnd.class, cls, AToA.OtherEnd.class , cls2);
			}
		}
		
		@From(Init.class) @To(LinkUnlinkAction.class)
		class InitToLinkUnlink extends Transition {}
	}
	
	class AToA extends Association
	{
		class ThisEnd extends One<A> {}
		class OtherEnd extends One<A> {}
	}
}
