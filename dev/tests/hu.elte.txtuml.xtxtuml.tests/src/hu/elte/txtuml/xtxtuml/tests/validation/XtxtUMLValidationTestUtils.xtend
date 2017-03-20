package hu.elte.txtuml.xtxtuml.tests.validation

class XtxtUMLValidationTestUtils {

	/**
	 * Returns the index of the <code>n</code>th occurrence of <code>pattern</code>
	 * in <code>text</code>, where <code>n</code> starts from zero. Returns <code>-1</code>
	 * if no such occurrence was found.
	 */
	def indexOfNth(String text, String pattern, int n) {
		if (n < 0) {
			return -1;
		}

		var currentIndex = text.indexOf(pattern, 0);
		var remaining = n;
		while (remaining > 0) {
			currentIndex = text.indexOf(pattern, currentIndex + 1);
			remaining--;
		}

		return currentIndex;
	}

}
