package hu.elte.txtuml.api.model.tests.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A <code>PrintStream</code> that puts the lines of the output in a list
 * instead of writing it to anywhere. Created with aim of testing the logs of
 * the <code>hu.elte.txtuml.api</code> package.
 * <p>
 * Only registers lines that were printed with the <code>println(String)</code>
 * method. The <code>hu.elte.txtuml.api</code> package always prints logs with
 * this method.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public class MemorizingPrintStream extends PrintStream {

	private List<String> lines = new ArrayList<>();

	public MemorizingPrintStream() {
		super(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
			}

		});
	}

	@Override
	public void println(String s) {
		lines.add(s);
	}

	public List<String> getOutput() {
		return lines;
	}

	public String[] getOutputAsArray() {
		return lines.toArray(new String[0]);
	}

}
