package com.news.toutiao.controller;

import com.news.toutiao.service.NewsService;
import com.news.toutiao.util.TouTiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by huali on 2018/1/17.
 */
@Controller
public class NewsController {
    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @RequestMapping(path={"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file)
    {
        try {
            String fileUrl = newsService.saveImage(file);
            if (fileUrl==null)
            {
                return  TouTiaoUtil.getJSONString(1,"上传图片失败");
            }
            return TouTiaoUtil.getJSONString(0,fileUrl);

        }catch (Exception e)
        {
            logger.error("上传图片失败"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"上传失败");

        }
    }

}