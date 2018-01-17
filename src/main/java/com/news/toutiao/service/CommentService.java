package com.news.toutiao.service;

import com.news.toutiao.dao.CommentDAO;
import com.news.toutiao.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huali on 2018/1/17.
 */
@Service
public class CommentService {
    //private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //
    //@Autowired
    //CommentDAO commentDAO;
    //
    //public List<Comment> getCommentsByEntity(int entityId, int entityType) {
    //    return commentDAO.selectByEntity(entityId, entityType);
    //}
    //
    //public int addComment(Comment comment) {
    //    return commentDAO.addComment(comment);
    //}
    //
    //public int getCommentCount(int entityId, int entityType) {
    //    return commentDAO.getCommentCount(entityId, entityType);
    //}
}