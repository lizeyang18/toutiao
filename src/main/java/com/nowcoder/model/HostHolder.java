package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by lizeyang on 2019/4/23.
 * function:表示当前的用户是谁，对接interceptor/PassportInterceptor
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
