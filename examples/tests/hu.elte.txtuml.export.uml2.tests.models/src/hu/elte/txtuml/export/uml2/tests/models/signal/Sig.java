package hu.elte.txtuml.export.uml2.tests.models.signal;

import hu.elte.txtuml.api.model.Signal;

public class Sig extends Signal {

	public Sig(int val, boolean b, String param) {
		super();
		this.val = val;
		this.b = b;
		this.param = param;
	}
	
	public int val;
	public boolean b;
	public String param;
}