package txtuml.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Transition {
    String from();
    String to();
}
