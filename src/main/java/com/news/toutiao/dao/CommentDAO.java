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
    //DAO层数据读取或者写入，列表读取

    String TABLE_NAME="comment";
    String INSERT_FIELDS="user_id,content,created_date,entity_id,entity_type,status";
    String SELECT_FILEDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
    ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);


    //删除评论。要写注解
    @Update({"update",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId,
                      @Param("entityType") int entityType,
                      @Param("status") int status);

    @Select({"select",SELECT_FILEDS,"from",TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc" })
    List<Comment> selectByEntity(@Param("entityId") int entityId,
                                 @Param("entityType") int entityType);

    @Select({"select count(id) from",TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                                 @Param("entityType") int entityType);
//增加分页功能，就要加offset和 limit
}
