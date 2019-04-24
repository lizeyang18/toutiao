package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.QiniuService;
import com.nowcoder.util.ToutiaoUtil;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * Created by lizeyang on 2019/4/23.
 * function:实现上传图片
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;


    //将图片返回给前端
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误"+e.getMessage());
        }
    }

    //增加一个咨询
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            News news = new News();
            if(hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名id
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加咨询错误"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
//            String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl == null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }

    }
}
