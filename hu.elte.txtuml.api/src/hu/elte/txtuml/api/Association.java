package hu.elte.txtuml.api;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;

public class Association implements ModelElement, LayoutLink {
	protected Association() {
	}

	public interface VisibleEnd<T extends ModelClass> extends ModelElement,
			Collection<T> {
	}

	public interface HiddenEnd<T extends ModelClass> extends ModelElement,
			Collection<T> {
	}

	public class Many<T extends ModelClass> extends DefaultMany<T> implements
			VisibleEnd<T> {
	}

	public class Some<T extends ModelClass> extends DefaultSome<T> implements
			VisibleEnd<T> {
	}

	public class MaybeOne<T extends ModelClass> extends DefaultMaybeOne<T>
			implements VisibleEnd<T> {
	}

	public class One<T extends ModelClass> extends DefaultOne<T> implements
			VisibleEnd<T> {
	}

	public abstract class Hidden {

		private Hidden() {
		}

		public class Many<T extends ModelClass> extends DefaultMany<T>
				implements HiddenEnd<T> {
		}

		public class Some<T extends ModelClass> extends DefaultSome<T>
				implements HiddenEnd<T> {
		}

		public class MaybeOne<T extends ModelClass> extends DefaultMaybeOne<T>
				implements HiddenEnd<T> {
		}

		public class One<T extends ModelClass> extends DefaultOne<T> implements
				HiddenEnd<T> {
		}

	}

}
