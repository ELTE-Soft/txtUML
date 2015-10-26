package hu.elte.txtuml.export.cpp.description;

public class ThreadPoolConfiguration {
	

	public class LinearFunction{
		private double gradient;
		private int constant;
		
		LinearFunction(double gradient, int constant){
			this.gradient = gradient;
			this.constant = constant;
		}
		
		public double getGradient(){
			return gradient;
		}
		
		public int getConstant(){
			return constant;
		}
		
	}
	
	ThreadPoolConfiguration(int id, double gradient, int constant){
		this.id = id;
		function = new LinearFunction(gradient,constant);
	}
	
	private LinearFunction function;
	
	private int id;
	private int threads;
	
	public int getId(){
		return id;
	}
	
	public int getNumberOfThreads(){
		return threads;
	}
	
	public LinearFunction getFunction(){
		return function;
	}
}
