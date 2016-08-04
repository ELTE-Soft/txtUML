package hu.elte.txtuml.api.model.seqdiag;

public class CombinedFragmentWrapper extends AbstractWrapper<String> {

	InteractionWrapper parent;
	CombinedFragmentType type;
	
	public CombinedFragmentWrapper(InteractionWrapper parent, CombinedFragmentType type,String fragmentName) {
		super(fragmentName);
	}

}
