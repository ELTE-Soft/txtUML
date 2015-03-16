package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.problems.MultiplicityException;

public abstract class AssociationEnd<T extends ModelClass> extends
		ModelIdentifiedElementImpl implements Collection<T> {

	protected boolean isFinal = true;
	private ModelIdentifiedElement owner;

	AssociationEnd() {
	}

	void setOwner(ModelIdentifiedElement newOwner) {
		owner = newOwner;
	}

	ModelIdentifiedElement getOwner() {
		return owner;
	}

	@Override
	public final ModelBool isEmpty() {
		return new ModelBool(count().getValue() == 0);
	}

	abstract boolean checkUpperBound();

	abstract boolean checkLowerBound();

	abstract AssociationEnd<T> init(Collection<T> other);

	abstract <S extends Collection<T>> S typeKeepingAdd(T object)
			throws MultiplicityException;

	abstract <S extends Collection<T>> S typeKeepingRemove(T object);

	@Override
	public abstract String toString();

}
