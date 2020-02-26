package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.domain.EntityType;
import com.nowcoder.domain.HostHolder;
import com.nowcoder.domain.News;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.utils.ToutiaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 用户点赞处理
     *
     * @param newsId
     * @return
     */
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(), newsId, EntityType.ENTITY_NEWS);
        /*更新喜欢数*/
        News news = newsService.getNewsById(newsId);
        newsService.updateLikeCount(newsId, (int) (likeCount));
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId()).setEntityId(newsId).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
    }

    /**
     * 用户点踩处理
     *
     * @param newsId
     * @return
     */
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId) {
        long likeCount = likeService.disLkie(hostHolder.getUser().getId(), newsId, EntityType.ENTITY_NEWS);
        /*更新喜欢数*/
        newsService.updateLikeCount(newsId, (int) (likeCount));
        return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
    }
}
