package txtuml.api;

abstract class ModelIdentifiedElementImpl implements ModelIdentifiedElement {

	private static Integer counter = 0;
	private final String identifier;

	protected ModelIdentifiedElementImpl() {
		synchronized (counter) {
			this.identifier = "inst_" + counter++;
		}
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}
}
