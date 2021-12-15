package j3_l7;

import j3_l7.annotations.AfterSuite;
import j3_l7.annotations.BeforeSuite;
import j3_l7.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        start(ClassTest.class);
    }

    private static<T> void start(Class<T> clazz) {
        try {
            T instance = clazz.getConstructor().newInstance();

            Method[] methods = clazz.getMethods();

            invokeBeforeSuite(methods, instance);
            invokeTestsByPriority(methods, instance);
            invokeAfterSuite(methods, instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static<T> void invokeBeforeSuite(Method[] methods, T instance) {
        Method beforeMethod = null;
        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null) {
                if (beforeMethod != null) {
                    throw new RuntimeException("Аннотаций BeforeSuite не может быть больше одной");
                }
                beforeMethod = method;
            }
        }

        if (beforeMethod != null) {
            try {
                beforeMethod.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static<T> void invokeAfterSuite(Method[] methods, T instance) {
        Method afterMethod = null;
        for (Method method : methods) {
            if (method.getAnnotation(AfterSuite.class) != null) {
                if (afterMethod != null) {
                    throw new RuntimeException("Аннотаций AfterSuite не может быть больше одной");
                }
                afterMethod = method;
            }
        }

        if (afterMethod != null) {
            try {
                afterMethod.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static<T> void invokeTestsByPriority(Method[] methods, T instance) {
        List<Priority> priorityList = new ArrayList<>();

        for (Method method : methods) {
            Test annotation = method.getAnnotation(Test.class);
            if (annotation != null) {
                priorityList.add(new Priority(method, annotation.priority()));
            }
        }

        priorityList.sort((o1, o2) -> o1.getPriority() - o2.getPriority());

        for (Priority priority : priorityList) {
            try {
                priority.getMethod().invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
