package com.nowcoder.async.handle;

import com.nowcoder.async.EventHandle;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.domain.Message;
import com.nowcoder.domain.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lizeyang on 2019/12/28.
 */

@Component
public class LikeHandler implements EventHandle {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NewsService newsService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户 " + user.getName() + " 赞了你的资讯: " + String.valueOf(newsService.getNewsById(model.getEntityId()).getTitle()));

        message.setFromId(model.getActorId());
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(model.getActorId() > model.getEntityOwnerId() ? String.format("%d_%d", model.getActorId(),model.getEntityOwnerId()) : String.format("%d_%d",model.getEntityOwnerId(), model.getActorId()));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
