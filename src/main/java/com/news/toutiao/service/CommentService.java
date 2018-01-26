package com.news.toutiao.service;

import com.news.toutiao.dao.CommentDAO;
import com.news.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huali on 2018/1/25.
 */
@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId,int entityType)
    {
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment)
    {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType)
    {
        return commentDAO.getCommentCount(entityId,entityType);
    }

    public void deleteComment(int entityId,int entityType)
    {
        commentDAO.updateStatus(entityId,entityType,1);
    }//封装，返回删除


}