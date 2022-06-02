package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.constant.Constant;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.workbench.domain.*;
import com.powernode.crm.workbench.mapper.*;
import com.powernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-05-18 9:57
 */
@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper ClueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public int insertNewClue(Clue clue) {
        return clueMapper.insertNewClue(clue);
    }

    @Override
    public List<Clue> queryAllClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryAllClueByConditionForPageForCount(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPageCount(map);
    }

    @Override
    public int updateClueById(Clue clue) {
        return clueMapper.updateClueById(clue);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectAllById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Constant.SESSION_USER);
        Clue clue = clueMapper.selectClueById(clueId);
        // customer部分
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setName(clue.getCompany());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        // 不要直接try-catch
        customerMapper.insertCustomer(customer);

        // contacts部分
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCustomerId(customer.getId()); // 先转客户再转联系人
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contactsMapper.insertContacts(contacts);

        // 备注部分
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        if (clueRemarks != null && clueRemarks.size() != 0) {
            // 转到客户这边
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            ContactsRemark contactsRemark = null;
            CustomerRemark customerRemark = null;
            for (ClueRemark clueRemark : clueRemarks) {
                // customer
                customerRemark = new CustomerRemark();
                customerRemark.setCustomerId(clueRemark.getId());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditTime(clueRemark.getNoteContent());
                customerRemarkList.add(customerRemark);
                // contacts
                contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(clueRemark.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemarkList.add(contactsRemark);
            }
            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
            contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
        }

        // 线索关联
        List<ClueActivityRelation> clueActivityRelationList = ClueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
        if (clueActivityRelationList != null && clueActivityRelationList.size() > 0) {
            ContactsActivityRelation contactsActivityRelation = null;
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);
        }

        // 创建交易
        if ("true".equals(map.get("isCreateTran"))) {
            // 对象封装
            Transaction transaction = new Transaction();
            transaction.setActivityId((String) map.get("activityId"));
            transaction.setCreateBy(user.getId());
            transaction.setContactsId(contacts.getId());
            transaction.setCreateTime(DateUtils.formatDateTime(new Date()));
            transaction.setCustomerId(customer.getId());
            transaction.setExpectedDate((String) map.get("expectedDate"));
            transaction.setId(UUIDUtils.getUUID());
            transaction.setMoney((String) map.get("money"));
            transaction.setName((String) map.get("name"));
            transaction.setOwner(user.getId());
            transaction.setStage((String) map.get("stage"));
            // 加入数据库
            transactionMapper.insertTransaction(transaction);

            // 交易备注表中要转移备注

            if (clueRemarks != null && clueRemarks.size() > 0) {
                List<TransactionRemark> transactionRemarkList = new ArrayList<>();
                TransactionRemark transactionRemark = null;
                for (ClueRemark clueRemark : clueRemarks) {
                    transactionRemark = new TransactionRemark();
                    transactionRemark.setCreateBy(clueRemark.getCreateBy());
                    transactionRemark.setCreateTime(clueRemark.getCreateTime());
                    transactionRemark.setEditBy(clueRemark.getEditBy());
                    transactionRemark.setEditFlag(clueRemark.getEditFlag());
                    transactionRemark.setEditTime(clueRemark.getEditTime());
                    transactionRemark.setNoteContent(clueRemark.getNoteContent());
                    transactionRemark.setId(UUIDUtils.getUUID());
                    transactionRemark.setTranId(transaction.getId());
                    transactionRemarkList.add(transactionRemark);
                }
                // 交易备注
                transactionRemarkMapper.insertTransactionRemarkByList(transactionRemarkList);
            }
        }

        // 统一删除评论
        clueRemarkMapper.deleteClueById(clueId);
        // 删除关联关系
        ClueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        // 删除这个线索
        clueMapper.deleteClueById(clueId);


    }

    @Override
    public int deleteClueByArray(String[] ids) {
        return clueMapper.deleteClueByArray(ids);
    }
}
