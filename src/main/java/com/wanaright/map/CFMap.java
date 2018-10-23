package com.wanaright.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CFMap {
    private CFMap() {
    }

    /**
     * map is key:list entry, will put an object into the value list, if list is null will new one
     */
    public static <T> List<T> putIntoList(Map<String, List<T>> map, String key, T value) {
        notNull(map);

        List<T> list = map.computeIfAbsent(key, k -> new ArrayList<>());
        list.add(value);
        return list;
    }

    private static void notNull(Map map) {
        if (map == null) {
            throw new NullPointerException();
        }
    }
}
