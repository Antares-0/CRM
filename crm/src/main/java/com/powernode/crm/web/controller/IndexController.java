package com.powernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LXM
 * @create 2022-04-24 15:16
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String Index() {
        // public的原因是为了公开，使得分发器能够调用得到
        return "index";
    }









}
