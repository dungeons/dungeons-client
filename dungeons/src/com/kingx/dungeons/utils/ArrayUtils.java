package com.kingx.dungeons.utils;


public class ArrayUtils {

    public static int overflow(int value, int size) {
        if (value < 0) {
            return value + size;
        } else if (value >= size) {
            return value - size;
        } else {
            return value;
        }
    }

}
