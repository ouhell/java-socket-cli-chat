package com.codebaker.utils;

public class Checks {
    static public boolean isNumber(Object obj) {
        return obj instanceof Integer || obj instanceof Float || obj instanceof Double || obj instanceof Long || obj instanceof Short || obj instanceof Byte;
    }
}
