package com.news.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.news.toutiao.util.TouTiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by huali on 2018/1/17.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "9NCSLph2Ee1XVYVUq7B20yzEMD4gGqMeGxWsoGql";
    String SECRET_KEY = "0jYnNE1Ycl_TsaMHUdSjO2t9ebpULxs6Lxbf5G1P";
    //要上传的空间
    String bucketname = "uestctoutiao";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //private static String QINIU_IMAGE_DOMAIN = "http://p2qp1rlg3.bkt.clouddn.com/";

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if(dotPos<0)
            {
                return null;
            }
            String fileExt=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
            if(!TouTiaoUtil.isFileAllowed(fileExt))
            {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(),fileName,getUpToken());
            //打印返回的信息
            System.out.println(res.toString());
            System.out.println(res.bodyString());
            //怎么解析res呢，这其实是一个json的格式，有hash, 有key
            //
            if(res.isOK()&&res.isJson())
            {
                String key = JSONObject.parseObject(res.bodyString()).get("key").toString();
                return TouTiaoUtil.QINIU_DOMAIN_PREFIX+ key;
            }
            else {
                logger.error("上传出错",res.bodyString());
                return  null;
            }

        } catch (QiniuException e) {
            logger.error("七牛异常"+e.getMessage());
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            return null;
        }
    }

    //自己添加用于从七牛云下载图片。
    public  String getDownloadUrl(String targeturl)
    {
        String downloadUrl = TouTiaoUtil.QINIU_DOMAIN_PREFIX+targeturl;
        return downloadUrl;
    }





}