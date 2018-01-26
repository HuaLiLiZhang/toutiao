package com.news.toutiao.dao;

import com.news.toutiao.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by huali on 2018/1/26.
 */
@Repository
@Mapper
public interface MessageDAO {
    String TABLE_NAME="message";
    String INSERT_FIELDS="from_id, to_id, content,has_read,conversation_id, created_date,";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") value(#{fromId},#{toId},#{content}," +
            "#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);



}
