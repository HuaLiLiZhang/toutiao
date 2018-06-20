package com.news.toutiao.controller;

import com.news.toutiao.model.*;
import com.news.toutiao.service.*;
import com.news.toutiao.util.TouTiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @Autowired
    LikeService likeService;





    //咨询的详情页。就是访问http://127.0.0.1:8080/news/6，
    // 会显示userID为6的用户的咨询的详情，以及评论。
    @RequestMapping(path={"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model)
    {
        News news = newsService.getById(newsId);

        if(news!=null) {
            //评论获取
            int localUserId = hostHolder.getUser()!=null ?hostHolder.getUser().getId():0;
            if(localUserId!=0)
            {
                model.addAttribute("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));

            }else {
                model.addAttribute("like",0);
            }
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
            //过滤content
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

            //过滤特殊字符，课后作业，发布敏感词需要过滤


        }catch (Exception e)
        {
            logger.error("增加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }






//    咨询的添加页面。
//展示图片
    @RequestMapping(path={"/image"},method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response)
    {
        try {
            response.setContentType("image/jpeg");
            //response.getOutputStream();
            //从本地下载图片到浏览器上。
            StreamUtils.copy(new FileInputStream(new File(TouTiaoUtil.IMAGE_DIR+imageName)),
                    response.getOutputStream());
        //    若从七牛云下载到浏览器查看呢?

        }catch (Exception e)
        {
            logger.error("读取图片错误"+e.getMessage());

        }

    }


    //从七牛云下载图片到本地。有待思考,可以显示了
    @RequestMapping(path ={"/download"}, method = RequestMethod.GET)
    //@ResponseBody
    public String download(Model model, @RequestParam("url") String targeturl,
                           HttpServletResponse response)
    {
        String qiniuyunUrl =  qiniuService.getDownloadUrl(targeturl);
        //StreamUtils.copy(new FileInputStream(new File(qiniuyunUrl)),
        //        response.getOutputStream());
        model.addAttribute("url", qiniuyunUrl);
        return "download";

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
                //匿名用户id，自己随便定义
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);

            newsService.addNews(news);
            //返回正确的
            return TouTiaoUtil.getJSONString(0);

        }catch (Exception e)
        {
            logger.error("添加资讯错误"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"发布失败");
        }
    }

    //上传图片
    //简单的图片上传,核心是MultipartFile file里面，所有数据都在MultipartFile里面
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam(value="file") MultipartFile file)
    {
                              //HttpServletRequest request {
        //if(request instanceof MultipartHttpServletRequest) {}
            try {
                //file.transferTo();
                //这里是保存到本地目录。简单的图片上传
                //String fileUrl = newsService.saveImage(file);
                //这里是保存到七流云，网上存储图片的一个缓存区。
                String fileUrl=qiniuService.saveImage(file);
                if (fileUrl == null) {
                    return TouTiaoUtil.getJSONString(1, "上传图片失败");
                }
                return TouTiaoUtil.getJSONString(0, fileUrl);
            //    返回一个URL的功能。
            } catch (Exception e) {
                logger.error("上传图片失败" + e.getMessage());
                return TouTiaoUtil.getJSONString(1, "上传失败");
            }

    }

}