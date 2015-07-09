package hu.elte.txtuml.layout.export.problems;

public interface ErrorMessages {

	static String moreThanOneLayoutInnerClassOfDiagram(Class<?> diagClass, Class<?> firstLayoutClass, Class<?> secondLayoutClass) {
		return "Two inner classes of " + diagClass.getName() + " (" + firstLayoutClass.getName() + " and " + secondLayoutClass.getName() + ") are subclasses of Diagram.Layout. Only one is permitted.";
	}
	
	static String noLayoutInnerClassOfDiagram(Class<?> diagClass) {
		return "Diagram class " +  diagClass.getName() + " has no inner class which is subclass of Diagram.Layout.";
	}
	
}
