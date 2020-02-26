package com.nowcoder;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.dao.TicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.domain.LoginTicket;
import com.nowcoder.domain.News;
import com.nowcoder.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by lizeyang on 2019/12/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class InitDatabaseTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private TicketDao ticketDao;

    @Test
    public void initDao() {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            /*添加用户*/
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", rand.nextInt(1000)));
            user.setPassword("12345");
            user.setSalt("");
            userDao.addUser(user);

            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);

            /*添加新闻*/
            News news = new News();
            news.setCommentCount(i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", rand.nextInt(1000)));
            news.setLink(String.format("http://images.nowcoder.com/%d.html", i));
            news.setLikeCount(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setUserId(i + 1);
            newsDao.addNews(news);

            /*添加ticket*/
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setExpired(date);
            loginTicket.setTicket(String.format("TICKET%d",i+1));
            loginTicket.setStatus(0);
            loginTicket.setUserId(i+1);
            ticketDao.addTicket(loginTicket);

            ticketDao.updateStatus(loginTicket.getTicket(),2);
        }
        System.out.println(ticketDao.selectByTicket("TICKET1").getStatus());
    }

    @Test
    public void updateTicket(){
        ticketDao.updateStatus("f0f39a6fb93747c49db0bf0ddb870278",1);
    }
}
