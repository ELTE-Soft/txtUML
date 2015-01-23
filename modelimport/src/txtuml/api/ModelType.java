package txtuml.api;

public class ModelType<T> extends ModelIdentifiedElementImpl {
	
	
	protected ModelType(T val) {
		this(val,true);
	
	}
	protected ModelType(T val,boolean literal, String expression)
	{
		this(val,literal,true,expression);
	}
	
	protected ModelType(T val,boolean literal,boolean calculated,String expression)
	{
		super();
		value=val;
		this.calculated=calculated;
		this.literal=literal;	
		this.expression=expression;
	}
	protected ModelType(T val,boolean literal)
	{
		this(val,literal,false,val.toString());	
	}
	protected ModelType() {
		this(null,false);
	}
	T getValue() {
		return value;
	}
	
	public String getExpression()
	{
		return expression;
	}
	
	public String toString() {
		return value.toString(); // TODO should not be used in the model
	}

	public ModelString toMString() {
		return new ModelString(value.toString());
	}
	
	private final T value;
	private final String expression;
	@SuppressWarnings("unused")
	private final boolean literal;
	@SuppressWarnings("unused")
	private final boolean calculated;

}

