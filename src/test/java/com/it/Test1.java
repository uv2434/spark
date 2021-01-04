package com.it;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class Test1 {
    @Test
    public void test1() {

        String s = "aabbcc";
        Map<String, Integer> map = new HashMap<>();

        for (String str : s.split("|")) {
            System.out.println(str);
            if (map.containsKey(str)) {
                map.put(str, map.get(str) + 1);
            } else {
                map.put(str, 1);
            }
        }
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println(e.getKey() + "_" + e.getValue() + "个");
        }
    }

    /**
     * 将输入的UNIX路径格式化，如/a/b/./../c，格式化为/a/c
     */
    @Test
    public void test2() {
        String path = "/a/b/./../c";
        String[] strings = path.split("/");

        path.lastIndexOf("/");
        System.out.println(StringUtils.ordinalIndexOf(path, "/",2));
        System.out.println(path.substring(0, StringUtils.ordinalIndexOf(path, "/",2))+1);
    }
}
