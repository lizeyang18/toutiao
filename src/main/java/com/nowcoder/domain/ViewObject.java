package com.nowcoder.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizeyang on 2019/12/24.
 * function:用于存储一条新闻user和news两个实体类
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
