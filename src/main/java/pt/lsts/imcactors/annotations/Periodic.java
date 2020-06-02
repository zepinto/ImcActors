package pt.lsts.imcactors.annotations;

public @interface Periodic {
    String value() default "1s";
}
