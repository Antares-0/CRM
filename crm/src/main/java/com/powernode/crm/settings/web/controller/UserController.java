package com.powernode.crm.settings.web.controller;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.settings.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-04-24 16:01
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * url要与处理完请求之后的页面资源目录保持一致，要带上.do
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        // 转发
        return "settings/qx/user/login";
    }


    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        // 转发，跳转到登陆首页
        Cookie cookie = new Cookie("loginAct", "1");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        Cookie cookie1 = new Cookie("loginPwd", "1");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        // 销毁session对象
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody //用于标识一个控制器方法，可以将该方法的返回值直接作为响应报文的响应体响应到浏览器
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest httpServletRequest, HttpSession session, HttpServletResponse response){
        System.out.println("进入");
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        // 根据查询结果判断
        ReturnObject returnObject = new ReturnObject();
        if (user == null){
            // 没查出来，密码错误或用户名错误
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号已过期");
        } else {
            // 查出来了，密码账号都对了
            // 需要进一步判断
            String nowStr = DateUtils.formatDateTime(new Date());
            if (nowStr.compareTo(user.getExpireTime()) < 0){ // 是否正确
                // 登陆失败，已经过期
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getLockState())){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            } else if (user.getAllowIps().contains(httpServletRequest.getRemoteAddr())){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("IP受限");
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                // 将User放到Session,每次登录的时候，我这边的服务器都可以实现将用户记住这样的一个小功能
                session.setAttribute(Constant.SESSION_USER, user);
                // 如果需要记住密码，就往外写cookie
                if ("true".equals(isRemPwd)){
                    Cookie cookie = new Cookie("loginAct", user.getLoginAct());
                    cookie.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(cookie);
                    Cookie cookie1 = new Cookie("loginPwd", user.getLoginPwd());
                    cookie1.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(cookie1);
                } else {
                    // 将没有过期的删除，删除不能直接操作底层文件，只能操作过期时间
                    Cookie cookie = new Cookie("loginAct", "1");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    Cookie cookie1 = new Cookie("loginPwd", "1");
                    cookie1.setMaxAge(0);
                    response.addCookie(cookie1);
                }
            }
        }
        return returnObject;
    }




}
