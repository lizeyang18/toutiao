package com.nowcoder.controller;


import com.nowcoder.domain.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.SensitiveWordFilter;
import com.nowcoder.utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by lizeyang on 2019/12/26.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping(path = {"/uploadImage/"}, method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtils.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtils.getJSONString(0, fileUrl);
        } catch (IOException e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtils.getJSONString(1, "上传失败");
        }
    }

   /* @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtils.IMAGE_DR + imageName)), response.getOutputStream());
        } catch (IOException e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }*/

    /**
     * 发布资讯
     *
     * @param image
     * @param title
     * @param link
     * @return
     */
    @RequestMapping(path = {"/user/addNews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            news.setImage(image);
            if (hostHolder != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
            /*设置一个匿名账户*/
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtils.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtils.getJSONString(1, "发布失败");
        }
    }

    /**
     * 获取资讯详情
     *
     * @param model
     * @param newsId
     * @return
     */
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(Model model, @PathVariable("newsId") int newsId) {
        try {
            News news = newsService.getNewsById(newsId);
            if (news != null) {
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<ViewObject> commentVos = new ArrayList<>();
                int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
                if (localUserId != 0) {
                    model.addAttribute("like", likeService.getLikeStatus(localUserId, news.getId(), EntityType.ENTITY_NEWS));
                } else {
                    model.addAttribute("like", 0);
                }

                for (Comment comment : comments) {
                    ViewObject commentVo = new ViewObject();
                    commentVo.set("comment", comment);
                    commentVo.set("user", userService.getUser(comment.getUserId()));
                    commentVos.add(commentVo);
                }
                model.addAttribute("comments", commentVos);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        } catch (Exception e) {
            logger.error("获取资讯错误" + e.getMessage());
        }
        return "detail";
    }

    /**
     * 资讯添加评论
     *
     * @param newsId
     * @param content
     * @return
     */
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
         /*加载敏感词库*/
        SensitiveWordFilter filter = new SensitiveWordFilter();
        /*比对敏感词*/
        Set<String> set = filter.getSensitiveWord(content, 1);

        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());

            if (set.size() > 0) {
                String replaceStr = "";
                for (int i = 0; i < set.size(); i++) {
                    replaceStr += "*";
                }
                content = content.replaceAll(set.toString(), replaceStr);
                comment.setContent(content + "(*为敏感词)");
            } else {
                comment.setContent(content);
            }
            comment.setStatus(0);
            comment.setCreatedDate(new Date());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            commentService.addComment(comment);

        /*更新评论数量，现在从news表中获取*/
            int commentCount = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(newsId, commentCount);
        } catch (Exception e) {
            logger.error("添加评论失败" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
}
