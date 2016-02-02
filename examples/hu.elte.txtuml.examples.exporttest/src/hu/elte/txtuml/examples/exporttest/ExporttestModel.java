package hu.elte.txtuml.examples.exporttest;

import hu.elte.txtuml.api.model.*;

class ExporttestModel extends Model {

	class SomeClass extends ModelClass {

		public void ifThen() {
			int x = 3;
			int y = 0;

			if (x > 2) {
				y = 4;
			}
			// System.out.println(y); // Causes a null pointer exception
		}

		public void ifThenElse() {
			int x = 3;
			int y;

			if (x > 2) {
				y = 4;
			} else {
				y = 2;
			}
			// System.out.println(y); // Causes a null pointer exception
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
			// System.out.println(y); // Causes a null pointer exception
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

			// q ^= 3; // NOT SUPPORTED
			// q &= 3; // NOT SUPPORTED
			// q |= 3; // NOT SUPPORTED
			// q >>= 3; // NOT SUPPORTED
			// q <<= 3; // NOT SUPPORTED
			// q >>>= 3; // NOT SUPPORTED

		}

		public void cycleTest() {
			int c = 0;
			for (int i = 0; i < 10; i++) {
				++c;
			}

			while (c < 20) {
				++c;
			}

//			do {
//				++c;
//			} while (c < 30);
//
//			int[] array = new int[] { 1, 2, 3, 4 };
//			for (int i : array) {
//				c += i;
//			}

		}

		// public void arrayTest() {
		// int[] array = new int[] { 1,2,3 };
		// array[0] = array[1] + array[2];
		// }

	}

}