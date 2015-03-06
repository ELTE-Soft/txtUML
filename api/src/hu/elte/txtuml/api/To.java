package hu.elte.txtuml.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface To {
	Class<? extends ModelClass.State> value();
}