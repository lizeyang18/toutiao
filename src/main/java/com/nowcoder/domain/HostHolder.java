package com.nowcoder.domain;

import org.springframework.stereotype.Component;

/**
 * Created by lizeyang on 2019/12/26.
 * function:当前访问的用户信息
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }

}
