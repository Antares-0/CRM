package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.domain.DictionaryValue;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.settings.service.DicValueService;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.workbench.domain.Activity;
import com.powernode.crm.workbench.domain.Clue;
import com.powernode.crm.workbench.domain.ClueActivityRelation;
import com.powernode.crm.workbench.domain.ClueRemark;
import com.powernode.crm.workbench.service.ActivityService;
import com.powernode.crm.workbench.service.ClueActivityRelationService;
import com.powernode.crm.workbench.service.ClueRemarkService;
import com.powernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author LXM
 * @create 2022-05-17 22:14
 */
@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userService.queryAllUsers();
        List<DictionaryValue> appellationList = dicValueService.queryAllDicValue("appellation");
        List<DictionaryValue> clueStateList = dicValueService.queryAllDicValue("clueState");
        List<DictionaryValue> sourceList = dicValueService.queryAllDicValue("source");
        // 把下拉列表中的所有数据都查出来
        request.setAttribute("userList", users);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String name, String company, String phone, String source, String owner, String mphone, String state, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("company", company);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Clue> clueList = clueService.queryAllClueByConditionForPage(map);
        int totalRows = clueService.queryAllClueByConditionForPageForCount(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("clueList", clueList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }


    @RequestMapping("/workbench/clue/insertNewClue.do")
    @ResponseBody
    public Object insertNewClue(Clue clue, HttpSession session) {
        ReturnObject object = new ReturnObject();
        clue.setId(UUIDUtils.getUUID());
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        try {
            int i = clueService.insertNewClue(clue);
            if (i > 0) {
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
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


    @RequestMapping("/workbench/clue/updateClueById.do")
    @ResponseBody
    public Object updateClueById(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setEditBy(user.getId());
        clue.setEditTime(DateUtils.formatDateTime(new Date()));
        ReturnObject object = new ReturnObject();
        try {
            int i = clueService.updateClueById(clue);
            if (i > 0) {
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                object.setMessage("添加成功！");
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙！请稍后重试...");
            }
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙！请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id) {
        ReturnObject object = new ReturnObject();
        Clue clue = clueService.queryClueById(id);
        return clue;
    }


    @RequestMapping("/workbench/clue/queryClueForDetail.do")
    public String queryClueForDetail(HttpServletRequest request, String id) {
        Clue clue = clueService.queryClueById(id);
        List<Activity> activities = activityService.queryActivityForDetailByClueId(id);
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkById(id);
        System.err.println(id);
        request.setAttribute("clue", clue);
        request.setAttribute("activityList", activities);
        request.setAttribute("remarkList", clueRemarks);
        return "workbench/clue/detail";
    }


    @RequestMapping("/workbench/clue/insertNewClueRemark.do")
    @ResponseBody
    public Object insertNewClueRemark(ClueRemark remark, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        ReturnObject object = new ReturnObject();
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtils.formatDateTime(new Date()));
        remark.setEditFlag(Constant.REMARK_EDIT_FLAG_NO_EDITED);
        remark.setId(UUIDUtils.getUUID());

        try {
            int i = clueRemarkService.insertClueRemark(remark);
            if (i > 0) {
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String clueId, String activityName) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        List<Activity> activities = activityService.selectActivityForDetailByNameClueId(map);
        // 直接返回，转变成字符串
        return activities;
    }


    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    // ids要和前台传入的保持一致
    public Object saveBund(String[] activityId, String clueId) {
        // 封装参数
        ClueActivityRelation clueActivityRelation = null;
        List<ClueActivityRelation> list = new ArrayList<>();
        for (String id : activityId) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setActivityId(id);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtils.getUUID());
            list.add(clueActivityRelation);
        }
        ReturnObject object = new ReturnObject();
        // 保存
        try {
            int i = clueActivityRelationService.saveCreateClueActivityRelationByList(list);
            if (i > 0) {
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityByIds(activityId);
                object.setRetData(activityList);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping("/workbench/clue/deleteRelation.do")
    @ResponseBody
    public Object deleteRelation(ClueActivityRelation clueActivityRelation) {
        ReturnObject object = new ReturnObject();
        try {
            int i = clueActivityRelationService.deleteCueActivityByClueIdActivityId(clueActivityRelation);
            if (i > 0) {
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("删除失败！");
            }
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("删除失败！");
            e.printStackTrace();
        }
        return object;
    }


    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        // 调用方法，查一下clue
        Clue clue = clueService.queryClueById(id);
        List<DictionaryValue> dictionaryValueList = dicValueService.queryAllDicValue("stage");
        request.setAttribute("clue", clue);
        request.setAttribute("stageList", dictionaryValueList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName, String clueId) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        // 查
        List<Activity> activities = activityService.queryActivityForConvertByNameClueId(map);
        return activities; // 直接return转换为json
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId, String money, String name, String expectedDate, String state, String activityId, String isCreateTran, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("clueId", clueId);
        map.put("money", money);
        map.put("name", name);
        map.put("expectedDate", expectedDate);
        map.put("state", state);
        map.put("isCreateTran", isCreateTran);
        map.put("activityId", activityId);
        map.put(Constant.SESSION_USER, session.getAttribute(Constant.SESSION_USER));

        ReturnObject object = new ReturnObject();
        try {
            clueService.saveConvert(map);
            object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping("/workbench/clue/updateClue.do")
    public Object updateClue(Clue clue, HttpSession session){
        ReturnObject object = new ReturnObject();
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        // 二次封装
        clue.setEditTime(DateUtils.formatDateTime(new Date()));
        clue.setEditBy(user.getId());
        try {
            int i = clueService.updateClueById(clue);
            if (i > 0){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping("/workbench/clue/deleteClue.do")
    public Object deleteClue(String[] ids){
        ReturnObject object = new ReturnObject();
        try {
            int i = clueService.deleteClueByArray(ids);
            if (i > 0){
                object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                object.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

}
