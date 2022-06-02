package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.domain.User;
import com.powernode.crm.workbench.domain.Customer;
import com.powernode.crm.workbench.domain.Transaction;
import com.powernode.crm.workbench.domain.TransactionHistory;
import com.powernode.crm.workbench.mapper.CustomerMapper;
import com.powernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.powernode.crm.workbench.mapper.TransactionMapper;
import com.powernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.acl.Owner;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-05-24 15:43
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public int insertCreateTransaction(Transaction transaction) {
        return transactionMapper.insertTransaction(transaction);
    }

    @Override
    public List<Transaction> queryTransactionForPageByCondition(Map<String, Object> map) {
        return transactionMapper.selectAllTransactionByConditionForPage(map);
    }

    @Override
    public int queryCountOfTransactionForPageByCondition(Map<String, Object> map) {
        return transactionMapper.selectCountOfTransactionByConditionForPage(map);
    }

    @Override
    public void saveCreateTransaction(Map<String, Object> map) {
        String customerName = (String)map.get("customerName");
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if (customer == null){
            customer =  new Customer();
            customer.setName(customerName);
            customer.setOwner(((User)map.get("user")).getId());
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customerMapper.insertCustomer(customer);
        }
        Transaction tran=new Transaction();
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtils.getUUID());
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));
        tran.setCreateBy(((User)map.get("user")).getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));
        transactionMapper.insertTransaction(tran);

        TransactionHistory tranHistory=new TransactionHistory();
        tranHistory.setCreateBy(((User)map.get("user")).getId());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        transactionHistoryMapper.insertTransactionHistory(tranHistory);
    }

    @Override
    public Transaction queryTransactionForDetailById(String id) {
        return transactionMapper.selectTranForDetailById(id);
    }
}
