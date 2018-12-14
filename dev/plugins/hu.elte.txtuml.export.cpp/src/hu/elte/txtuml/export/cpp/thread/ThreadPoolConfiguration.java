package hu.elte.txtuml.export.cpp.thread;

public class ThreadPoolConfiguration {

	private int id;
	private double rate;

	public ThreadPoolConfiguration(int id, double rate) {
		this.id = id;
		this.rate = rate;

	}

	public void decraseId() {
		id = id - 1;
	}

	public int getId() {
		return id;
	}
	
	public double getRate() {
		return rate;
	}
}

