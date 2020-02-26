package com.nowcoder.service;

import com.nowcoder.dao.TicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.domain.LoginTicket;
import com.nowcoder.domain.User;
import com.nowcoder.utils.ToutiaoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lizeyang on 2019/12/24.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketDao ticketDao;

    /**
     * 根据userId查询用户
     *
     * @param id
     * @return
     */
    public User getUser(int id) {
        return userDao.selectById(id);
    }


    /**
     * 根据username获取User
     *
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        return userDao.selectByName(username);
    }

    /**
     * 实现用户注册
     *
     * @param username
     * @param password
     * @return
     */
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        /*用户名是否为空*/
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        /*密码不能为空*/
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        /*验证是否已注册*/
        User user = userDao.selectByName(username);
        if (user != null) {
            map.put("msgname", "用户已经被注册!");
            return map;
        }

        /*（用户未注册）加强密码*/
        user = new User();
        user.setName(username);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(ToutiaoUtils.MD5(password + user.getSalt()));
        userDao.addUser(user);

        /*注册转登录*/
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 实现用户登录
     *
     * @param username
     * @param password
     * @return
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        /*用户名是否为空*/
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        /*密码不能为空*/
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDao.selectByName(username);
        /*用户不存在*/
        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }

        /*验证密码*/
        if (ToutiaoUtils.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        /*登录*/
        String ticket = addLoginTicket(user.getId());

        /*删除之前的ticket记录:name+失效ticket*/
//        ticketDao.delTicket(new Date().getTime());

        map.put("ticket", ticket);
        return map;
    }


    /**
     * 伴随注册和登录添加Ticket验证信息
     *
     * @param userId
     * @return
     */
    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));

        Date date = new Date();
        /*设置ticket默认有效时间为24小时*/
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        loginTicket.setExpired(date);
        ticketDao.addTicket(loginTicket);

        return loginTicket.getTicket();
    }

    /**
     * 用户登出：设置ticket的status为1
     *
     * @param ticket
     */
    public void logout(String ticket) {
        ticketDao.updateStatus(ticket, 1);
    }
}
