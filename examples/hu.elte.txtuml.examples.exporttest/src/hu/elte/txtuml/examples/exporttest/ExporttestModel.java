package hu.elte.txtuml.examples.exporttest;

import hu.elte.txtuml.api.model.*;

class ExporttestModel extends Model {

	class OtherClass extends ModelClass {

		public OtherClass(int i) {
		}
		
	}
	
	class OtherClassWithCtor extends OtherClass {
		
		public OtherClassWithCtor() {
			this(0);
		}
		
		public OtherClassWithCtor(int i) {
			super(i);
		}
		
	}
	
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

		public void switchCase() {
//			int x = 3;
//			int y;
//
//			switch (x) {
//			case 2:
//				y = 2;
//				break;
//			case 3:
//				return;
//			default:
//				y = 1;
//			}
		}
		
		public void earlyReturn() {
			int x = 1;
			if (x == 1) {
				return;
			}
			x = 2;
		}

		public int fld;

		public void compoundOps() {
			int q = fld;
			boolean b = false;

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
		
		public void foreachCycle() {
//			int c = 0;
//			???
//			for (int i : list) {
//				c += i;
//			}

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

}