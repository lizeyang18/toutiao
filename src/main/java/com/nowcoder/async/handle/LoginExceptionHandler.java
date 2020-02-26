package com.nowcoder.async.handle;

import com.nowcoder.async.EventHandle;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.domain.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lizeyang on 2019/12/28.
 */

@Component
public class LoginExceptionHandler implements EventHandle {
    @Autowired
    private MessageService messageService;

/*    @Autowired
    private MailSender mailSender;*/

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setCreatedDate(new Date());
        /*假设userId=20是系统*/
        message.setFromId(20);
        message.setHasRead(0);
        message.setContent("你上次的登录IP异常");
        message.setConversationId(model.getActorId() > model.getEntityOwnerId() ? String.format("%d_%d", model.getActorId(), model.getEntityOwnerId()) : String.format("%d_%d", model.getEntityOwnerId(), model.getActorId()));
        message.setToId(model.getActorId());
        messageService.addMessage(message);

       // Map<String, Object> map = new HashMap<>();
       // map.put("username", model.getExt("username"));
       // mailSender.sendWithHTMLTemplate(model.getExt("to"), "登录异常", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
