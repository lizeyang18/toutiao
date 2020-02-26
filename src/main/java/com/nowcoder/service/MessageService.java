package com.nowcoder.service;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    /**
     * 添加消息
     *
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        return messageDao.addMessage(message);
    }

    /**
     * 获取消息列表
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId, offset, limit);
    }

    /**
     * 获取未读消息总数
     *
     * @param userId
     * @param conversationId
     * @return
     */
    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDao.getConbersationUnreadCount(userId, conversationId);
    }

    /**
     * 获取消息总数
     *
     * @param userId
     * @param conversationId
     * @return
     */
    public int getConversationTotalCount(int userId, String conversationId) {
        return messageDao.getConbersationTotalCount(userId, conversationId);
    }

    /**
     * 获取站内信详细消息
     *
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    /**
     * 更新消息状态：未读->已读
     *
     * @param userId
     * @param conversationId
     */
    public void updateHasRead(int userId, String conversationId) {
        messageDao.updateHasRead(userId, conversationId);
    }

}
