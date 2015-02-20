package txtuml.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO upcoming feature
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Multiplicity {
	int lowerBound() default 0;
	int higherBound() default -1;
}