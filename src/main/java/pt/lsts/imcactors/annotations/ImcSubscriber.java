package pt.lsts.imcactors.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ImcSubscriber {
    String entity() default "";
    String source() default "";
    boolean loopback() default false;
}
