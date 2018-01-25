package com.news.toutiao.controller;

import com.news.toutiao.model.*;
import com.news.toutiao.service.CommentService;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.QiniuService;
import com.news.toutiao.service.UserService;
import com.news.toutiao.util.TouTiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;






    @RequestMapping(path={"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model)
    {
        News news = newsService.getById(newsId);
        if(news!=null) {
            //评论获取
            List<Comment> comments=commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs=new ArrayList<ViewObject>();
            for(Comment comment  :comments)
            {
                ViewObject vo=new ViewObject();
                vo.set("comment",comment);
                vo.set("user",userService.getUser(comment.getUserId()));
                commentVOs.add(vo);

            }
            model.addAttribute("comments",commentVOs);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }


    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content)
    {
        try {
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            //更新news里面的评论数量
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);

            //怎么异步化


        }catch (Exception e)
        {
            logger.error("增加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }


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