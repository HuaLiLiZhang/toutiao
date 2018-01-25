package com.news.toutiao.dao;

import com.news.toutiao.model.Comment;
import jdk.nashorn.internal.objects.annotations.Where;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huali on 2018/1/25.
 */
@Repository
@Mapper

public interface CommentDAO {
    String TABLE_NAME="comment";
    String INSERT_FIELDS="user_id,content,created_date,entity_id,entity_type,status";
    String SELECT_FILEDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
    ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);


    @Select({"select",SELECT_FILEDS,"from",TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc" })
    List<Comment> selectByEntity(@Param("entityId") int entityId,
                                 @Param("entityType") int entityType);

    @Select({"select count(id) from",TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                                 @Param("entityType") int entityType);

}
