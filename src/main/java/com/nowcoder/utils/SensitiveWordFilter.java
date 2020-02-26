package com.nowcoder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizeyang on 2019/12/29.
 * function:敏感词过滤
 */
public class SensitiveWordFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordFilter.class);

    public Map sensitiveWordMap = null;
    public static int minMatchType = 1;  //最小匹配规则
    public static int maxMatchType = 2;  //最大匹配规则

    public SensitiveWordFilter() {
        sensitiveWordMap = new SensitiveWordInit().initKeyWord();
    }

    /*判断文字是否包含敏感字符*/
    public boolean isContainSensitiveWord(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = this.CheckSensitiveWOrd(txt, i, matchType);
            /*大于0存在，返回true*/
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /*检查文字中是否包含敏感字符，检查规则如下*/
    public int CheckSensitiveWOrd(String txt, int beginIndex, int matchType) {
        /*敏感词结束标识，用于敏感词只有1位的情况*/
        boolean flag = false;
        /*匹配标识数默认为0*/
        int matchFlag = 0;
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            /*存在则判断是否为最后一个*/
            if (nowMap != null) {
                /*找到相应的key，匹配标识+1*/
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    if (SensitiveWordFilter.minMatchType == matchType) {
                        break;
                    }
                }
            } else {  //不存在，直接返回
                break;
            }
        }
        /*长度必须大于1，为词*/
        if (matchFlag < 2 && !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

    /*获取文字中的敏感词*/
    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWoldList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            /*判断是否包含敏感词*/
            int length = CheckSensitiveWOrd(txt, i, matchType);
            /*存在，加入到list中*/
            if (length > 0) {
                sensitiveWoldList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWoldList;
    }
}
