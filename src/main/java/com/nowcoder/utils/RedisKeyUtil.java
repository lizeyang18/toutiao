package com.nowcoder.utils;

/**
 * Created by lizeyang on 2019/12/27.
 * function:确保key值唯一
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LINKE";
    private static String BIZ_DISLIKE = "DISLINKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getBizEvent() {
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getBizDisLikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
