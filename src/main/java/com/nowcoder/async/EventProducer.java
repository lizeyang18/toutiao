package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.utils.JedisUtils;
import com.nowcoder.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lizeyang on 2019/12/28.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private JedisUtils jedisUtils;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String key = RedisKeyUtil.getBizEvent();
            String json = JSON.toJSONString(eventModel);
            jedisUtils.lpush(key, json);
            logger.info("已识别事件");
            return true;
        } catch (Exception e) {
            logger.error("异步队列入队失败" + e.getMessage());
            return false;
        }
    }
}
