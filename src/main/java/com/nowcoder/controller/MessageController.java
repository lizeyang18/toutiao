package com.nowcoder.controller;

import com.nowcoder.domain.HostHolder;
import com.nowcoder.domain.Message;
import com.nowcoder.domain.User;
import com.nowcoder.domain.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.SensitiveWordFilter;
import com.nowcoder.utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 添加消息
     *
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {


        Message message = new Message();
        message.setContent(content);
        message.setCreatedDate(new Date());
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(fromId > toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        message.setHasRead(0);
        messageService.addMessage(message);

        return ToutiaoUtils.getJSONString(message.getId());
    }


    /**
     * 获取站内信列表
     *
     * @param model
     * @return
     */
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> messages = messageService.getConversationList(localUserId, 0, 10);
            for (Message message : messages) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", message);
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    /**
     * 获取站内信列表的详细消息
     *
     * @param model
     * @param conversationId
     * @return
     */
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            for (Message message : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                User user = userService.getUser(message.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);

                /*更新当前用户的消息状态*/
                if (hostHolder.getUser().getId() == message.getToId()) {
                    messageService.updateHasRead(hostHolder.getUser().getId(), conversationId);
                }
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取站内信消息失败" + e.getMessage());
        }
        return "letterDetail";
    }
}
