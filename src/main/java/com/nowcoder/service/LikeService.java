package com.nowcoder.service;

import com.nowcoder.utils.JedisUtils;
import com.nowcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Service
public class LikeService {
    @Autowired
    private JedisUtils jedisUtils;

    /**
     * 获取当前用户对这个资讯的喜欢还是不喜欢
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if (jedisUtils.sismember(likeKey, String.valueOf(entityId))) {
            return 1;
        }
        /*1表示喜欢，-1表示不喜欢*/
        String disLikeKey = RedisKeyUtil.getBizDisLikeKey(entityId, entityType);
        return jedisUtils.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    /**
     * 当前用户like之后添加“喜欢”键值对
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public long like(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        /*在喜欢里添加键值对*/
        jedisUtils.sadd(likeKey, String.valueOf(userId));
        /*在不喜欢里删除键值对*/
        String disLikeKey = RedisKeyUtil.getBizDisLikeKey(entityId, entityType);
        jedisUtils.srem(disLikeKey, String.valueOf(userId));
        /*获取当前帖子的点赞数*/
        return jedisUtils.scard(likeKey);
    }

    /**
     * 当前用户dislike之后添加“不喜欢”键值对
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public long disLkie(int userId, int entityId, int entityType) {
        String disLikeKey = RedisKeyUtil.getBizDisLikeKey(entityId, entityType);
        /*在不喜欢里添加键值对*/
        jedisUtils.sadd(disLikeKey, String.valueOf(userId));
        /*在喜欢里删除键值对*/
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisUtils.srem(likeKey, String.valueOf(userId));
        /*获取当前帖子的点赞数：因为多个用户共同访问，需要查询*/
        return jedisUtils.scard(likeKey);
    }
}
