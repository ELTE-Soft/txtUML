package txtuml.api;

// defines compile errors and warnings for the model
privileged aspect ModelRules {
	// TODO needs improvement
	private pointcut withinModel() : within(ModelElement+) && !within(txtuml.api..*);
	private pointcut method(): execution(* *(..));
	private pointcut methodOfSomeTypeExtendsModelClass(): execution(* ModelClass+.*(..));
	private pointcut methodNotOfSomeTypeExtendsModelClass(): method() && !methodOfSomeTypeExtendsModelClass();
	private pointcut methodVoidOrReturnsSomeTypeExtendsModelClassOrModelType(): execution((void || ModelClass+ || ModelType+) *(..));
		
	declare error: methodOfSomeTypeExtendsModelClass() && !methodVoidOrReturnsSomeTypeExtendsModelClassOrModelType() && !within(ModelClass) :
		"a ModelClass method must have a return type of void, ModelClass, ModelType or any subclass of the last two";
}
