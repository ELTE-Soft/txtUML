package hu.elte.txtuml.export.uml2.tests.models.compound_ops;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public int fld;
	
	public void test() {
		boolean var = false;
		
		++fld;
		--fld;
		fld++;
		fld--;
		fld = 10;
		fld += 10;
		fld -= 10;
		fld *= 10;
		fld /= 10;
		fld %= 10;
		
		var &= true;
		var |= false;
	}
	
}
