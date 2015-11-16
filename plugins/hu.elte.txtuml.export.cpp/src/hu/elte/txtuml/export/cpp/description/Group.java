package hu.elte.txtuml.export.cpp.description;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.export.cpp.description.GroupContainer;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GroupContainer.class)
public @interface Group{
	Class<? extends ModelClass>[] contains();
	double gradiend() default 0;
	int constant() default 1;
}