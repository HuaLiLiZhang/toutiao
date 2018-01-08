package com.news.toutiao.dao;

import com.news.toutiao.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by huali on 2018/1/8.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";

    String INSERT_FIELDS = " name, password, salt, head_url ";

    String SELECT_FIELDS = " id, name, password, salt, head_url ";

    @Insert({
            "insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{name}, #{password}, #{salt}, #{headUrl})"
    })
    int addUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    User selectById(int id);

}
