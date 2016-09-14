package hu.elte.txtuml.export.cpp.thread;

public class ThreadPoolConfiguration {

	public class LinearFunction {
		private double gradient;
		private int constant;

		LinearFunction(double gradient, int constant) {

			setGradient(gradient);
			setConstant(constant);
		}

		public double getGradient() {
			return gradient;
		}

		public int getConstant() {
			return constant;
		}

		public void setGradient(double gradient) {
			if (gradient >= 0 && gradient <= 1) {
				this.gradient = gradient;
			} else if (gradient < 0) {
				this.gradient = 0;
			} else if (gradient > 1) {
				this.gradient = 1;
			}

		}

		public void setConstant(int constant) {
			if (constant < 0) {
				this.constant = 0;
			} else {
				this.constant = constant;
			}
		}

	}

	private LinearFunction function;
	private int id;
	private int maxThread;

	public ThreadPoolConfiguration(int id, double gradient, int constant) {
		this.id = id;
		function = new LinearFunction(gradient, constant);

	}

	public void decraseId() {
		id = id - 1;
	}

	public int getId() {
		return id;
	}

	public int getMaxThread() {
		return maxThread;
	}

	public LinearFunction getFunction() {
		return function;
	}

	public void setMaxThreads(int max) {
		if (max < 1) {
			maxThread = 1;
		} else {
			maxThread = max;
		}
	}
}
