package com.news.toutiao.dao;

import com.news.toutiao.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huali on 2018/1/26.
 */
@Repository
@Mapper
public interface MessageDAO {
    String TABLE_NAME="message";
    String INSERT_FIELDS="from_id, to_id, content,has_read,conversation_id, created_date";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ",SELECT_FIELDS," from ", TABLE_NAME," where conversation_id=#{conversationId}" +
            "order by id desc limit #{offset},#{limit}"})
    List<Message> getConservationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset")int offset,
                                        @Param("limit")int limit);


    @Select({"select",INSERT_FIELDS,",count(id) as id from (select * from",TABLE_NAME,"" +
            "where from_id=#{userId} or to_id=#{userId} order by id desc)table group by " +
            "conversationId order by id desc limit#{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

}
