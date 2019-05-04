package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lizeyang on 2019/5/2.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        //将收到的事件放到队列里面
        try {
            String json = JSONObject.toJSONString(eventModel);  //序列化
            String key = RedisKeyUtil.getEventQueueKey();  //放在Redis队列里面
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
