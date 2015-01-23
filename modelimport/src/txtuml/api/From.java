package txtuml.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface From {
    Class<? extends ModelClass.State> value();
}