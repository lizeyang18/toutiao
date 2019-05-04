package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lizeyang on 2019/5/3.
 * function:判断是否有异常登录，否则发出站内信提醒
 */
public class LoginExceptionHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        //判断是否有异常登录
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登录IP异常!");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
