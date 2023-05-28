package edu.csf.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ApplicationContext {
    public static <T> T getInstance(Class<T> tClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        var instance = tClass.getDeclaredConstructor().newInstance();
        String className = tClass.getName();
        String packageName = className.substring(0, className.indexOf(".controller") + 1);
        className = className.substring(className.lastIndexOf("."), className.length() - 10);
        className = packageName + "persistence" + className + "CsvFileDAO";

        for (Field field : tClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(DI.class)) {
                var dependencyClass = Class.forName(className);
                var dependencyInstance = dependencyClass.getDeclaredConstructor().newInstance();
                field.setAccessible(true);
                field.set(instance, dependencyInstance);
                field.setAccessible(false);
            }
        }
        return instance;
    }

}
