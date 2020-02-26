package com.nowcoder.controller;

import com.nowcoder.dao.UserDao;
import com.nowcoder.domain.EntityType;
import com.nowcoder.domain.HostHolder;
import com.nowcoder.domain.News;
import com.nowcoder.domain.ViewObject;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizeyang on 2019/12/24.
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 通过ViewObject的Map形式将User和News组合，共同显示一条新闻
     * userId为0表示查询所有用户的所有新闻，否则根据userId查询该用户发布的新闻
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);

        List<ViewObject> vos = new ArrayList<>();
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, news.getId(), EntityType.ENTITY_NEWS));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 主页：查询所有用户的所有新闻
     *
     * @param model
     * @return
     */
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getNews(0, 0, 10));
        return "home";
    }

    /**
     * 根据userId查询该用户新闻
     *
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }

    /**
     * 用户访问某些页面验证合法性，不满足则拦截跳转至指定页
     *
     * @return
     */
    @RequestMapping(path = "/setting")
    @ResponseBody
    public String setting() {
        return "setting : OK";
    }
}
