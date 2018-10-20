package control.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class OutputSignal extends Signal{
	public int drumRPM;
	public double temperature;
	public boolean doDrain;
	public boolean doFill;
	public boolean openTray1;
	public boolean openTray2;
	public boolean openTray3;
	
	public OutputSignal(
			int drumRMP,
			double temperature,
			boolean doDrain,
			boolean doFill,
			boolean openTray1,
			boolean openTray2,
			boolean openTray3
			) {
		this.drumRPM = drumRMP;
		this.temperature = temperature;
		this.doDrain = doDrain;
		this.doFill = doFill;
		this.openTray1 = openTray1;
		this.openTray2 = openTray2;
		this.openTray3 = openTray3;
	}
}
