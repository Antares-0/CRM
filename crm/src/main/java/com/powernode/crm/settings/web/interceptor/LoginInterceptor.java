package com.powernode.crm.settings.web.interceptor;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author LXM
 * @create 2022-05-05 9:50
 */
public class LoginInterceptor implements HandlerInterceptor {

    // 根据session进行登陆验证
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        if (user == null){
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()); // 自己重定向的时候要带上名字
            return false;
        } else {
            return true; // false表示拦截，true表示放行
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
