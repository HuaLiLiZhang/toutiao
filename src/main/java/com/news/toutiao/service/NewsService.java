package com.news.toutiao.service;

import com.news.toutiao.dao.CommentDAO;
import com.news.toutiao.dao.NewsDAO;
import com.news.toutiao.model.News;
import com.news.toutiao.util.TouTiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

    public int addNews(News news)
    {
        newsDAO.addNews(news);
        return  news.getId();
    }
    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        //这个得到后缀名。
        if (!TouTiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }
        //UUID随机生成文件名。
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(TouTiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return TouTiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    //    返回的地址，URL是是返回给前端的
    }

    public int updateCommentCount(int id,int count)
    {
        return newsDAO.updateCommentCount(id,count);
    }

    public int updateLikeCount(int id,int likeCount)
    {
        return newsDAO.updateLikeCount(id,likeCount);
    }


}