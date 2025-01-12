package com.dollar.penguin.util;

import java.util.Map;

public class LoginContextHolder<T> {

    private static final ThreadLocal<Map<String, Object>> LOGIN_THREAD_LOCAL = new ThreadLocal<>();

    public static Map<String, Object> get() {
        return LOGIN_THREAD_LOCAL.get();
    }

    public static void set(Map<String, Object> value) {
        LOGIN_THREAD_LOCAL.set(value);
    }

    public static void clear() {
        LOGIN_THREAD_LOCAL.remove();
    }
}
