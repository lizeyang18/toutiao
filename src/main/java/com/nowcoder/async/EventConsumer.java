package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.utils.JedisUtils;
import com.nowcoder.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizeyang on 2019/12/28.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;
    /*EventType是Jedis里面的阻塞对列的key（阻塞队列的唯一标志），config表示继承EventHandle接口的所有实现类集合，这里相当于链路反调，每一个EventType有哪几个EventHandle要处理*/
    private Map<EventType, List<EventHandle>> config = new HashMap<>();

    @Autowired
    private JedisUtils jedisUtils;

    /*形成一张路由表，通过映射（EventType）找到所有能够处理的实现类*/
    @Override
    public void afterPropertiesSet() throws Exception {
        /*找到所有实现EventHandle接口的实现类*/
        Map<String, EventHandle> beans = applicationContext.getBeansOfType(EventHandle.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandle> entry : beans.entrySet()) {
                /*找到EventHandle接口里面的所有EventType*/
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandle>());
                        logger.info("已添加事件处理 " + eventType);
                    }

                    /*注入EventType对应的doHandle方法*/
                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        /*启动线程去消费事件*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    /*从阻塞队列一直取元素*/
                    String key = RedisKeyUtil.getBizEvent();
                    List<String> events = jedisUtils.brpop(0, key);

                    for (String event : events) {
                        if (event.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(event, EventModel.class);
                    /*找到这个事件的处理handle列表*/
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandle handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                        logger.info("已完成事件处理");
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
