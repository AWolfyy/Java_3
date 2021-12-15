package j3_l7;

import java.lang.reflect.Method;

public class Priority {
    private final Method method;
    private final int priority;

    public Priority(Method method, int priority) {
        this.method = method;
        this.priority = priority;
    }

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }
}
