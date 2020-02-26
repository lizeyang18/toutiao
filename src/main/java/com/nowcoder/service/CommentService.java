package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }
}
