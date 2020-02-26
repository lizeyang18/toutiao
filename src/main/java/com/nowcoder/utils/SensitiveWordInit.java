package com.nowcoder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by lizeyang on 2019/12/29.
 * function:初始化敏感词库
 */
public class SensitiveWordInit {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordInit.class);

    private String ENCODING = "UTF8";
    public HashMap sensitiveWordMap;

    public SensitiveWordInit() {
        super();
    }

    public Map initKeyWord() {
        try {
            /*读取敏感词库*/
            Set<String> keyWordSet = readSensitiveWordFile();
            /*将敏感词加入到HashMap中*/
            addSensitiveWordToHashMap(keyWordSet);
            /*//spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /*将得到的敏感词库用一个DFA算法模型放到Map中*/
    public void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        /*初始化敏感词容器*/
        sensitiveWordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWordMap = null;
        /*迭代keyWordSet*/
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                /*如果存在该key，直接赋值*/
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    /*不存在则构建一个map，同时将isEnd设置为0*/
                    newWordMap = new HashMap<>();
                    newWordMap.put("isEnd", "0");  //不是最后一个
                    nowMap.put(keyChar,newWordMap);
                    nowMap = newWordMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");  //最后一个
                }
            }
        }
    }

    /*读取敏感词文件，加到Set集合中*/
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        /*读取文件*/
        File file = new File("D:\\SensitiveWord.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
        try {
            if (file.isFile() && file.exists()) {
                set = new HashSet<>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                /*读取文件，将文件内容放到set中*/
                while ((txt = bufferedReader.readLine()) != null) {
                    set.add(txt);
                }
            } else {
                throw new Exception("敏感词库不存在");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();
        }
        return set;
    }
}
