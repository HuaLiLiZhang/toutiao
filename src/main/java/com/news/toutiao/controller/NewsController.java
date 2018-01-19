package com.news.toutiao.controller;

import com.news.toutiao.model.HostHolder;
import com.news.toutiao.model.News;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.QiniuService;
import com.news.toutiao.util.TouTiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * Created by huali on 2018/1/17.
 */
@Controller
public class NewsController {
    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;


//展示图片
    @RequestMapping(path={"/image"},method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response)
    {
        try {
            response.setContentType("image/jpeg");
            //response.getOutputStream();
            StreamUtils.copy(new FileInputStream(new File(TouTiaoUtil.IMAGE_DIR+imageName)),
                    response.getOutputStream());
        }catch (Exception e)
        {
            logger.error("读取图片错误"+e.getMessage());

        }



    }


    @RequestMapping(path={"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link)
    {
        try {
            News news =new News();
            if(hostHolder.getUser()!=null)
            {
               news.setUserId(hostHolder.getUser().getId());
            }else
            {
                //匿名用户id
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);

            newsService.addNews(news);
            return TouTiaoUtil.getJSONString(0);

        }catch (Exception e)
        {
            logger.error("添加资讯错误"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"发布失败");
        }
    }

    //上传图片

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam(value="file") MultipartFile file)
    {
                              //HttpServletRequest request {
        //if(request instanceof MultipartHttpServletRequest) {}
            try {
                //file.transferTo();
                //String fileUrl = newsService.saveImage(file);
                String fileUrl=qiniuService.saveImage(file);
                if (fileUrl == null) {
                    return TouTiaoUtil.getJSONString(1, "上传图片失败");
                }
                return TouTiaoUtil.getJSONString(0, fileUrl);
            } catch (Exception e) {
                logger.error("上传图片失败" + e.getMessage());
                return TouTiaoUtil.getJSONString(1, "上传失败");
            }

    }

}