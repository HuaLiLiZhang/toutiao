package com.news.toutiao.service;

import com.news.toutiao.dao.NewsDAO;
import com.news.toutiao.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huali on 2018/1/9.
 */
@Service
public class NewsService {
    @Autowired
    public NewsDAO newsDAO;

    public List<News> getLatestNews(int userId,int offset,int limit)
    {
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

}