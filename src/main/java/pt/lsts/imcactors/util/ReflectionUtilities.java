package pt.lsts.imcactors.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtilities {

    public static List<Method> getAnnotatedMethods(Class<? extends Annotation> annotation, Class<?> clazz) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getAnnotation(annotation) != null)
                methods.add(m);
        }

        if (clazz != Object.class)
            methods.addAll(0, getAnnotatedMethods(annotation, clazz.getSuperclass()));

        return methods;
    }


}
