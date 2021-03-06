package com.news.toutiao.dao;

import com.news.toutiao.model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huali on 2018/1/8.
 */

@Repository
@Mapper
public interface NewsDAO {
    String TABLE_NAME="news";
    String INSERT_FIELDS="title,link,image,like_count,comment_count,created_date,user_id";
    String SELECT_FILEDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
    ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FILEDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);


    //这个使用XML文件配置
    List<News> selectByUserIdAndOffset(@Param("userId") int userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);



}
