package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lizeyang on 2019/12/26.
 * function:通过SpringMVC的适配器将拦截器注入到链路中，使其生效
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*注意:拦截器优先级顺序*/
        registry.addInterceptor(passportInterceptor);
        /*设置访问某些页面验证其身份，不满足则通过loginRequiredInterceptor拦截至指定页*/
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
