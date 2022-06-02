package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.workbench.domain.ActivityRemark;
import com.powernode.crm.workbench.service.ActivityRemarkService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author LXM
 * @create 2022-05-17 10:40
 */
@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveActivityRemark.do")
    @ResponseBody
    public Object saveActivityRemark(ActivityRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        // 二次封装
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formatDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Constant.REMARK_EDIT_FLAG_NO_EDITED);
        ReturnObject object = new ReturnObject();
        try {
            int i = activityRemarkService.insertActivityRemark(remark);
            if (i > 0){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                object.setRetData(remark);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
        }
        return object;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String activityRemarkId){
        ReturnObject object = new ReturnObject();
        try {
            int i = activityRemarkService.deleteActivityRemark(activityRemarkId);
            if (i == 1){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                object.setMessage("删除成功！");
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
        }
        return object;
    }

    @RequestMapping("/workbench/activity/updateActivityRemark.do")
    @ResponseBody
    public Object updateActivityRemark(String id, String noteContent, HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setEditFlag(Constant.REMARK_EDIT_FLAG_YES_EDITED);
        activityRemark.setEditBy(user.getId());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        ReturnObject object = new ReturnObject();
        try {
            int i = activityRemarkService.updateActivityRemark(activityRemark);
            if (i > 0){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                object.setRetData(activityRemark);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
        }
        return object;
    }





}
