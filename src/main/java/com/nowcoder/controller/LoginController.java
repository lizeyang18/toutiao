package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by lizeyang on 2019/12/25.
 */

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 实现用户注册
     *
     * @param model
     * @param username
     * @param password
     * @param rememberMe
     * @param response
     * @return
     */
    @RequestMapping(path = "/reg/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            /*如果注册成功则产生一条ticket，进而添加cookie，登录*/
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                /*设置cookie的生命周期是全局*/
                cookie.setPath("/");
                if (rememberMe > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);   //记住我：5天有效时间
                }
                response.addCookie(cookie);
                /*0表示注册成功，0之外均是注册失败*/
                logger.info("注册成功");
                return ToutiaoUtils.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtils.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtils.getJSONString(1, "注册异常");
        }
    }

    /**
     * 实现用户登录
     *
     * @param model
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    @RequestMapping(path = "/login/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            /*如果登录成功则产生一条ticket，进而添加cookie*/
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                /*设置cookie的生命周期是全局*/
                cookie.setPath("/");
                if (rememberMe > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);   //记住我：5天有效时间
                }
                response.addCookie(cookie);
                /*0表示注册成功，0之外均是注册失败*/
                logger.info("登录成功");
                eventProducer.fireEvent(new
                        EventModel(EventType.LOGIN).setActorId(userService.findUserByName(username).getId())
                        .setExt("username", username).setExt("to", username));
                return ToutiaoUtils.getJSONString(0, "登录成功");
            } else {
                return ToutiaoUtils.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return ToutiaoUtils.getJSONString(1, "登录异常");
        }
    }

    /**
     * 实现用户登出
     *
     * @param ticket
     * @return
     */
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
