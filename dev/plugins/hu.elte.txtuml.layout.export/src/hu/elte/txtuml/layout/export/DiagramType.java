package hu.elte.txtuml.layout.export;

/**
 * Possible types of a diagram.
 */
public enum DiagramType {
	Unknown, Class, StateMachine, Composite;

	public hu.elte.txtuml.layout.visualizer.model.DiagramType convert() {
		switch (this) {
		case Class:
			return hu.elte.txtuml.layout.visualizer.model.DiagramType.Class;
		case StateMachine:
			return hu.elte.txtuml.layout.visualizer.model.DiagramType.State;
		case Composite:
			return hu.elte.txtuml.layout.visualizer.model.DiagramType.Composite;
		case Unknown:
		default:
			return hu.elte.txtuml.layout.visualizer.model.DiagramType.unknown;
		}
	}
}
