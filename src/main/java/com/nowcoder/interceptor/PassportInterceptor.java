package com.nowcoder.interceptor;

import com.nowcoder.dao.TicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.domain.HostHolder;
import com.nowcoder.domain.LoginTicket;
import com.nowcoder.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by lizeyang on 2019/12/26.
 * function:拦截Controller，验证用户信息是否合法
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketDao ticketDao;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        /*验证ticket是否有效*/
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        /*ticket有值，需要进一步判断其状态status(0表示正常)和有效期expired*/
        if (ticket != null) {
            LoginTicket loginTicket = ticketDao.selectByTicket(ticket);
            if (ticket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            /*验证通过，将User存到HostHolder供下一步使用*/
            User user = userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        /*通过modelAndView即后端与前端渲染交互的工具，将User传到前端*/
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        /*hostHolder是每个用户建立的单独线程，在完成拦截器后需要关闭，否则消耗内存*/
        hostHolder.clear();
    }
}
