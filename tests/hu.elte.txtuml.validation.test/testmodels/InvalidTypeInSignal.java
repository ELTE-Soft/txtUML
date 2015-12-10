import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class InvalidTypeInSignal extends Signal {
	int x;
	A a;
	Object obj;
}

class A extends ModelClass {
}