package hu.elte.txtuml.export.javascript.scalers;

public abstract class NodeScaler {
	protected Integer width;
	protected Integer height;
	
	public Integer getWidth(){
		if (width == null){
			estimateWidth();
		}
		return width;
	}
	
	public Integer getHeight(){
		if (height == null){
			estimateHeight();
		}
		return height;
	}

	protected abstract void estimateHeight();
	protected abstract void estimateWidth();
}
