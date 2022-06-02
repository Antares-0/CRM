package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.HSSFUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.workbench.domain.Activity;
import com.powernode.crm.workbench.domain.ActivityRemark;
import com.powernode.crm.workbench.service.ActivityRemarkService;
import com.powernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @author LXM
 * @create 2022-05-05 20:47
 */
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest httpServletRequest) {
        // 调用service方法
        List<User> users = userService.queryAllUsers();
        httpServletRequest.setAttribute("userList", users);
        return "workbench/activity/index";
    }

    // 这里是异步请求，而且返回的内容作为一个对象整体返回
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        // 将activity进行二次封装,将一些没有填充的属性加上
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityService.saveCreateActivity(activity);
            if (i > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            // 写数据会产生异常
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Activity> activities = activityService.queryActivityByConditionForPage(map);
        int integer = activityService.queryCountOfActivityByCondition(map);
        Map<String, Object> retMap = new HashMap<>();
        return retMap;
    }


    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityService.deleteActivityByIds(id);
            if (i > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }


    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        // 还要对编辑属性进行注入
        User login_user = (User) session.getAttribute(Constant.SESSION_USER);
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        activity.setEditBy(login_user.getId());
        try {
            int i = activityService.updateActivityById(activity);
            if (i == 1) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
            e.printStackTrace();
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/fileDown.do")
    public void fileDown(HttpServletResponse response) throws Exception {
        // 返回excel文件，设置文件返回信息，指明返回的是一个二进制文件
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 获取输出流，只能写字符流，写二进制文件不能用这个response.getWriter();
        OutputStream outputStream = response.getOutputStream();
        // 浏览器接收到相应信息后，默认在浏览器中直接打开，即使浏览器自己打不开，也会调用电脑自带的软件，实在打不开才会激活文件下载窗口
        // 可以设置响应头信息，使浏览器接收到响应信息后，直接激活文件下载窗口，即使能打开也不打开
        // 设置文件名
        response.addHeader("Content-Disposition","attachment;filename=myStudentList.xls");
        // 读取excel文件到内存
        InputStream fileInputStream = new FileInputStream("C:\\Users\\10727\\Desktop\\my.xls");
        // 缓冲区
        byte[] buff = new byte[256];
        // 写
        int len = 0;
        while ((len = fileInputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
        }
        // 关闭资源，只关闭自己创建的
        fileInputStream.close();
        // 这个不关闭，使用刷新，outputStream会由Tomcat关闭
        outputStream.flush();
    }

    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws Exception{
        List<Activity> activities = activityService.queryAllActivity();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        // a.id, b.name as owner, a.name, a.start_date, a.end_date, a.cost, a.description, a.create_time,
        //               c.name as create_by, a.edit_time, d.name as edit_by
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("所有者");
        cell = row.createCell(1);
        cell.setCellValue("活动名称");
        cell = row.createCell(2);
        cell.setCellValue("开始日期");
        cell = row.createCell(3);
        cell.setCellValue("结束日期");
        cell = row.createCell(4);
        cell.setCellValue("成本");
        cell = row.createCell(5);
        cell.setCellValue("活动描述");
        cell = row.createCell(6);
        cell.setCellValue("创建时间");
        cell = row.createCell(7);
        cell.setCellValue("创建者");
        cell = row.createCell(8);
        cell.setCellValue("最后修改时间");
        cell = row.createCell(9);
        cell.setCellValue("最后修改人");
        Activity cur = null; // 放在外面，效率高，不用每次创建一个新的对象
        for (int i = 1; i <= activities.size(); i++){
            cur = activities.get(i - 1);
            HSSFRow row1 = sheet.createRow(i);
            cell = row1.createCell(0);
            cell.setCellValue(cur.getOwner());
            cell = row1.createCell(1);
            cell.setCellValue(cur.getName());
            cell = row1.createCell(2);
            cell.setCellValue(cur.getStartDate());
            cell = row1.createCell(3);
            cell.setCellValue(cur.getEndDate());
            cell = row1.createCell(4);
            cell.setCellValue(cur.getCost());
            cell = row1.createCell(5);
            cell.setCellValue(cur.getDescription());
            cell = row1.createCell(6);
            cell.setCellValue(cur.getCreateTime());
            cell = row1.createCell(7);
            cell.setCellValue(cur.getCreateBy());
            cell = row1.createCell(8);
            cell.setCellValue(cur.getEditTime());
            cell = row1.createCell(9);
            cell.setCellValue(cur.getEditBy());
        }
        // 根据work对象生成excel文件
//        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\10727\\Desktop\\crm.xls");
//        workbook.write(fileOutputStream); // 运行非常差劲，因为访问了磁盘
//        workbook.close();
//        fileOutputStream.close();
        // 创建客户端输出流
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=AllActivity.xls");
        ServletOutputStream outputStream = response.getOutputStream();
//        InputStream inputStream = new FileInputStream("C:\\Users\\10727\\Desktop\\crm.xls");
        workbook.write(outputStream);
//        byte[] buff = new byte[256];
//        int len = 0;
//        while ((len = inputStream.read(buff)) != - 1){
//            outputStream.write(buff, 0, len); // 效率低
//        }
//        inputStream.close();
        workbook.close();
        outputStream.flush();
    }


    @RequestMapping("/workbench/activity/exportActivityByIds.do")
    public void exportActivityByIds(HttpServletResponse response, HttpServletRequest request) throws Exception{
        String[] ids = request.getParameter("id").split("&");
        List<Activity> activities = activityService.queryActivityByIds(ids);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("所有者");
        cell = row.createCell(1);
        cell.setCellValue("活动名称");
        cell = row.createCell(2);
        cell.setCellValue("开始日期");
        cell = row.createCell(3);
        cell.setCellValue("结束日期");
        cell = row.createCell(4);
        cell.setCellValue("成本");
        cell = row.createCell(5);
        cell.setCellValue("活动描述");
        cell = row.createCell(6);
        cell.setCellValue("创建时间");
        cell = row.createCell(7);
        cell.setCellValue("创建者");
        cell = row.createCell(8);
        cell.setCellValue("最后修改时间");
        cell = row.createCell(9);
        cell.setCellValue("最后修改人");
        Activity cur = null; // 放在外面，效率高，不用每次创建一个新的对象
        for (int i = 1; i <= activities.size(); i++){
            cur = activities.get(i - 1);
            HSSFRow row1 = sheet.createRow(i);
            cell = row1.createCell(0);
            cell.setCellValue(cur.getOwner());
            cell = row1.createCell(1);
            cell.setCellValue(cur.getName());
            cell = row1.createCell(2);
            cell.setCellValue(cur.getStartDate());
            cell = row1.createCell(3);
            cell.setCellValue(cur.getEndDate());
            cell = row1.createCell(4);
            cell.setCellValue(cur.getCost());
            cell = row1.createCell(5);
            cell.setCellValue(cur.getDescription());
            cell = row1.createCell(6);
            cell.setCellValue(cur.getCreateTime());
            cell = row1.createCell(7);
            cell.setCellValue(cur.getCreateBy());
            cell = row1.createCell(8);
            cell.setCellValue(cur.getEditTime());
            cell = row1.createCell(9);
            cell.setCellValue(cur.getEditBy());
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=SelectedActivity.xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }


    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        ReturnObject object = new ReturnObject();
        String id = ((User) session.getAttribute(Constant.SESSION_USER)).getId();
        try {
            // 把接收到的文件写道磁盘上
            InputStream inputStream = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> list = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++){
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(id); // 谁导入的谁负责
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(id);
                for (int j = 0; j < row.getLastCellNum(); j++){
                    cell = row.getCell(j);
                    String cellStr = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0){
                        activity.setName(cellStr);
                    } else if (j == 1){
                        activity.setStartDate(cellStr);
                    } else if (j == 2){
                        activity.setEndDate(cellStr);
                    } else if (j == 3){
                        activity.setCost(cellStr);
                    } else if (j == 4){
                        activity.setDescription(cellStr);
                    }
                }
                list.add(activity);
            }
            // 调用方法导入数据
            int i = activityService.saveActivityByList(list);
            object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            object.setMessage("导入成功！共" + i + "条数据被导入！");
        } catch (IOException e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("服务器忙，请稍后重试...");
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping("/workbench/activity/queryActivityForDetailsById.do")
    public String queryActivityForDetailsById(String id, HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailsById(id);
        List<ActivityRemark> activityRemarks = activityRemarkService.queryActivityRemarkForDetailById(activity.getId());
        request.setAttribute("activity", activity);
        request.setAttribute("remarks", activityRemarks);
        // 请求转发
        return "workbench/activity/detail";
    }






}
