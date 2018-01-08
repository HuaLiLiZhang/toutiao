package dao;

import com.news.toutiao.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by huali on 2018/1/8.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME="user";
    String INSERT_FIELDS="name,password,salt,head_url";
    String SELECT_FIELDS="id,name,password,salt,head_url";

    //@Insert({"insert into user(name, password, slat, head_url) value()"})
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
            ")"," value(#{name},#{password},#{salt},#{head_url})"})
    int addUser(User user);

}
