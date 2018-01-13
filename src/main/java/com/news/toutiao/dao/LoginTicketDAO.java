package com.news.toutiao.dao;

import com.news.toutiao.model.LoginTicket;
import com.news.toutiao.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by huali on 2018/1/12.
 */
@Repository
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME="login_ticket";
    String INSERT_FIELDS="user_id,expired,status,ticket";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    User selectByTicket(String ticket);

    @Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") String status);

}
