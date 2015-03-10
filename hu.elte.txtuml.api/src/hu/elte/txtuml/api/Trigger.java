package hu.elte.txtuml.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Trigger {
	Class<? extends Signal> value();
}
