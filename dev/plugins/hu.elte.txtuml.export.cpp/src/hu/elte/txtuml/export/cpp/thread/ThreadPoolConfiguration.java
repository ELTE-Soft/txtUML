package hu.elte.txtuml.export.cpp.thread;

public class ThreadPoolConfiguration {

	private int id;
	private int numberOfExecutors;

	public ThreadPoolConfiguration(int id, int numberOfExecutors) {
		this.id = id;
		this.numberOfExecutors = numberOfExecutors;

	}

	public int getId() {
		return id;
	}
	
	public int getNumberOfExecutors () {
		return numberOfExecutors;
	}
}

