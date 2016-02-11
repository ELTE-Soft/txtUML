package hu.elte.txtuml.examples.exporttest;

import hu.elte.txtuml.api.model.ModelClass;

class SomeClass extends ModelClass {

	public void ifThen() {
		int x = 3;
		int y = 0;

		if (x > 2) {
			y = 4;
		}
	}

	public void ifThenElse() {
		int x = 3;
		int y;

		if (x > 2) {
			y = 4;
		} else {
			y = 2;
		}
	}

	public int fld;

	public void compoundOps() {
		int q = fld;
		boolean b = false;
		Integer x = q;

		fld += 10;
		fld -= 20;
		fld *= 5;
		fld /= 3;
		fld %= 2;

		q = 2;

		b &= true;
		b |= true;
	}

	public void forCycle() {
		int c = 0;
		for (int i = 0; i < 10; i++) {
			++c;
		}
	}

	public void whileCycle() {
		int c = 0;
		while (c < 20) {
			++c;
		}
	}

	public void doCycle() {
		int c = 0;
		do {
			++c;
		} while (c < 30);
	}

	public void foreachCycle(hu.elte.txtuml.api.model.Collection<Integer> coll) {
		int c = 0;
		for (int i : coll) {
			c = i;
		}

	}

	public int returnTest() {
		return 10;
	}

	public void noReturnTest() {
		return;
	}

	public void constructorTest() {
		new OtherClass(4);
		new OtherClassWithCtor();
	}

}