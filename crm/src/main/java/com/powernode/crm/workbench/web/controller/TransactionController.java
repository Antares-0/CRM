package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.domain.DictionaryValue;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.settings.mapper.DictionaryValueMapper;
import com.powernode.crm.settings.service.DicValueService;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.workbench.domain.*;
import com.powernode.crm.workbench.mapper.ContactsMapper;
import com.powernode.crm.workbench.mapper.CustomerMapper;
import com.powernode.crm.workbench.mapper.TransactionMapper;
import com.powernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.powernode.crm.workbench.service.*;
import com.sun.org.apache.bcel.internal.Const;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Target;
import java.util.*;

/**
 * @author LXM
 * @create 2022-05-24 12:45
 */
@Controller
public class TransactionController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRemarkService transactionRemarkService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {
        // 调用service方法查动态数据
        List<DictionaryValue> dictionaryValueList = dicValueService.queryAllDicValue("transactionType");
        List<DictionaryValue> dictionaryValueList1 = dicValueService.queryAllDicValue("source");
        List<DictionaryValue> dictionaryValueList2 = dicValueService.queryAllDicValue("stage");
        request.setAttribute("transactionTypeList", dictionaryValueList);
        request.setAttribute("sourceList", dictionaryValueList1);
        request.setAttribute("stageList", dictionaryValueList2);
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DictionaryValue> stageList = dicValueService.queryAllDicValue("stage");
        List<DictionaryValue> transactionTypeList = dicValueService.queryAllDicValue("transactionType");
        List<DictionaryValue> sourceList = dicValueService.queryAllDicValue("source");
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/transaction/save";
    }


    @RequestMapping("/workbench/transaction/toEdit.do")
    public String toEdit(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DictionaryValue> stageList = dicValueService.queryAllDicValue("stage");
        List<DictionaryValue> transactionTypeList = dicValueService.queryAllDicValue("transactionType");
        List<DictionaryValue> sourceList = dicValueService.queryAllDicValue("source");
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/transaction/edit";
    }

    @RequestMapping("/workbench/transaction/queryAllActivityForSave.do")
    @ResponseBody
    public Object queryAllActivityForSave(String name) {
        List<Activity> activities = activityService.queryActivityForTransactionByName(name);
        return activities;
    }

    @RequestMapping("/workbench/transaction/queryAllContactsForSave.do")
    @ResponseBody
    public Object queryAllContactsForSave(String name) {
        List<Contacts> contacts = contactsService.queryAllContactsByName(name);
        return contacts;
    }

    @RequestMapping("/workbench/transaction/saveCreateTransaction.do")
    @ResponseBody
    public Object saveCreateTransaction(@RequestParam Map<String, Object> map, HttpSession session) {
        ReturnObject object = new ReturnObject();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        map.put("user", user);
        try {
            transactionService.saveCreateTransaction(map);
            object.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            object.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后...");
            e.printStackTrace();
        }
        return object;

    }

    // 根据页面提供的条件字段进行查询
    @RequestMapping("/workbench/transaction/queryTransactionByConditionForPage.do")
    @ResponseBody
    public Object queryTransactionByConditionForPage(String owner, String name, String customerId, String stage, String type, String source, String contactsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("customerId", customerId);
        map.put("stage", stage);
        map.put("type", type);
        map.put("source", source);
        map.put("contactsId", contactsId);
        List<Transaction> transactions = transactionService.queryTransactionForPageByCondition(map);
        // 还有一个页数的查询
        int i = transactionService.queryCountOfTransactionForPageByCondition(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("transactionList", transactions);
        retMap.put("totalRows", i);
        return retMap;
    }


    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stage) {
        // 获取可能性
        ResourceBundle possibility = ResourceBundle.getBundle("possibility"); // 因为是在根目录下，直接放在这里面
        return possibility.getString(stage);
    }

    @RequestMapping("workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String customerName) {
        return customerService.queryAllCustomerName(customerName);
    }


    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id, HttpServletRequest request) {

        Transaction tran = transactionService.queryTransactionForDetailById(id);
        List<TransactionRemark> remarkList = transactionRemarkService.queryTransactionRemarkForDetailByTranId(id);
        List<TransactionHistory> historyList = transactionHistoryService.queryAllTranHistoryForDetail(id);


        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());

        tran.setPossibility(possibility);


        request.setAttribute("tran", tran);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("historyList", historyList);
        //request.setAttribute("possibility",possibility);


        //

        List<DictionaryValue> stageList = dicValueService.queryAllDicValue("stage");
        request.setAttribute("stageList", stageList);

        //请求转发
        return "workbench/transaction/detail";
    }





}