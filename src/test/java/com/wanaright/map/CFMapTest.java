package com.wanaright.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFMapTest {
    @Test
    public void putIntoList() {
        Map<String, List<String>> map = new HashMap<>();

        CFMap.putIntoList(map, "number", "1");

        Assert.assertEquals(map.size(), 1);
        Assert.assertEquals(map.get("number").get(0), "1");

        CFMap.putIntoList(map, "number", "2");

        Assert.assertEquals(map.size(), 1);
        Assert.assertEquals(map.get("number").size(), 2);
    }
}