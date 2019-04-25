package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.websocket.server.ServerEndpoint;
import java.util.List;

/**
 * Created by lizeyang on 2019/4/25.
 */
@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment)
    {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }
}
