package txtuml.metamodel;

import txtuml.api.*;

public class MetaModel {
	public static class Model extends ModelClass {
		public ModelString name;
	}
	public static class Class extends ModelClass {
		public ModelString name;
	}
	public class ClassesInModel extends txtuml.api.Association {
		@One Model containingModel;
		@Many Class containedClass;
	}
	public class Association extends ModelClass {
		
	}
	public class AssociationsInModel extends txtuml.api.Association {
		@One Model containingModel;
		@Many Association containedAssociation;
	}
	public class Method extends ModelClass {
		
	}
	public class MethodsInModel extends txtuml.api.Association {
		@One Model containingModel;
		@Many Method containedMethod;
	}
	public class Event extends ModelClass {
		
	}
	public class EventInModel extends txtuml.api.Association {
		@One Model containingModel;
		@Many Event containedEvent;
	}
	public class Attribute extends ModelClass {
		
	}
	public class AttributesInClass extends txtuml.api.Association {
		@One Class containingClass;
		@Many Attribute containedAttribute;
	}
	public class MethodsInClass extends txtuml.api.Association {
		@One Class containingClass;
		@Many Method containedMethod;
	}
	public class StateMachine extends ModelClass {
		
	}
	public class StateMachineInClass extends txtuml.api.Association {
		@One Class containingClass;
		@Many StateMachine containedStateMachine;
	}
	
}
