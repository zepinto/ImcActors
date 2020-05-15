package pt.lsts.imcactors.annotations;

import pt.lsts.imc4j.msg.Message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ImcPublisher {
    Class<? extends Message>[] value();
}
